package com.xueyi.gen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.gen.GenConstants.OptionField;
import com.xueyi.common.core.constant.gen.GenConstants.TemplateType;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.text.CharsetKit;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.web.entity.service.impl.SubBaseServiceImpl;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.manager.GenTableManager;
import com.xueyi.gen.mapper.GenTableColumnMapper;
import com.xueyi.gen.mapper.GenTableMapper;
import com.xueyi.gen.service.IGenTableColumnService;
import com.xueyi.gen.service.IGenTableService;
import com.xueyi.gen.util.GenUtils;
import com.xueyi.gen.util.VelocityInitializer;
import com.xueyi.gen.util.VelocityUtils;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.feign.RemoteMenuService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class GenTableServiceImpl extends SubBaseServiceImpl<GenTableDto, GenTableManager, GenTableMapper, GenTableColumnDto, IGenTableColumnService, GenTableColumnMapper> implements IGenTableService {

    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private RemoteMenuService remoteMenuService;

    /**
     * 获取后端代码生成地址
     *
     * @param table    业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getGenPath(GenTableDto table, String template) {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, StrUtil.SLASH)) {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }

    /**
     * 获取前端代码生成地址
     *
     * @param table    业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getUiPath(GenTableDto table, String template) {
        String uiPath = table.getUiPath();
        if (StringUtils.equals(uiPath, StrUtil.SLASH)) {
            return System.getProperty("user.dir") + File.separator + "MultiSaas-UI" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return uiPath + File.separator + VelocityUtils.getFileName(template, table);
    }

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableList(GenTableDto genTableDto) {
        return baseManager.selectDbTableList(genTableDto);
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] tableNames) {
        return baseManager.selectDbTableListByNames(tableNames);
    }

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    @Override
    @DSTransactional
    public void importGenTable(List<GenTableDto> tableList) {
        try {
            tableList.forEach(table -> {
                GenUtils.initTable(table);
                int row = baseManager.insert(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumnDto> columnList = subService.selectDbTableColumnsByName(table.getName());
                    columnList.forEach(column -> GenUtils.initColumnField(column, table));
                    subService.insertBatch(columnList);
                    GenUtils.initTableOptions(columnList, table);
                    baseManager.update(table);
                }
            });
        } catch (Exception e) {
            throw new ServiceException("导入失败：" + e.getMessage());
        }
    }

    /**
     * 修改业务
     *
     * @param table 业务信息
     */
    @Override
    @DSTransactional
    public int update(GenTableDto table) {
        int row = baseManager.update(table);
        if (row > 0)
            GenUtils.updateCheckColumn(table);
        table.getSubList().forEach(column -> subService.update(column));
        return row;
    }

    /**
     * 预览代码
     *
     * @param id Id
     * @return 预览数据列表
     */
    @Override
    public List<JSONObject> previewCode(Long id) {
        List<JSONObject> dataMap = new ArrayList<>();
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        JSONObject data;
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            data = new JSONObject();
            String vmName = StrUtil.subAfter(template, StrUtil.SLASH, true);
            vmName = StrUtil.removeSuffix(vmName, (".vm"));
            data.put("name", vmName);
            data.put("language", StrUtil.subAfter(vmName, StrUtil.DOT, true));
            data.put("template", sw.toString());
            dataMap.add(data);
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     *
     * @param id Id
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(id, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param id Id
     */
    @Override
    public void generatorCode(Long id) {
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        String[] genFiles = {"merge.java.vm", "mergeMapper.java.vm", "dto.java.vm", "po.java.vm", "controller.java.vm", "service.java.vm", "serviceImpl.java.vm", "manager.java.vm", "mapper.java.vm", "sql.sql.vm"};
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                String path = StrUtil.containsAny(template, genFiles)
                        ? getGenPath(table, template)
                        : getUiPath(table, template);
                FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
            } catch (IOException e) {
                throw new ServiceException("渲染模板失败，表名：" + table.getName());
            }
        }
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    @DSTransactional
    public void syncDb(String tableName) {
        GenTableDto table = baseManager.selectDbTableByName(tableName);
        List<GenTableColumnDto> columnList = table.getSubList();
        List<String> columnNames = columnList.stream().map(GenTableColumnDto::getName).collect(Collectors.toList());

        List<GenTableColumnDto> dbTableColumns = subService.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns)) {
            throw new ServiceException("同步数据失败，原表结构不存在");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumnDto::getName).collect(Collectors.toList());
        dbTableColumns.forEach(column -> {
            if (!columnNames.contains(column.getName())) {
                GenUtils.initColumnField(column, table);
                subService.insert(column);
            }
        });

        List<Long> delColumnIds = columnList.stream().filter(column -> !dbTableColumnNames.contains(column.getName())).map(GenTableColumnDto::getId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumnIds)) {
            subService.deleteByIds(delColumnIds);
        }
    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param ids Ids数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long[] ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Long id : ids) {
            generatorCode(id, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(Long id, ZipOutputStream zip) {

        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, HttpConstants.Character.UTF8.getCode());
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getName(), e);
            }
        }
    }

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        checkTclBasic(genTable, optionsObj);
        switch (Objects.requireNonNull(TemplateType.getValue(genTable.getTplCategory()))) {
            case SUB_BASE:
                checkTclSub(optionsObj);
                checkTclBase(genTable, optionsObj);
                break;
            case SUB_TREE:
                checkTclSub(optionsObj);
            case TREE:
                checkTclTree(optionsObj);
            case BASE:
                checkTclBase(genTable, optionsObj);
        }
    }

    /**
     * 校验基础表配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBasic(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.SOURCE_MODE.getCode()))) {
            throw new ServiceException("未设置源策略模式！");
        }
        if (StrUtil.isNotEmpty(optionsObj.getString(OptionField.IS_TENANT.getCode())) && StrUtil.equals(optionsObj.getString(OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode())) {
            for (GenTableColumnDto column : genTable.getSubList()) {
                if (StrUtil.equalsAny(column.getJavaField(), GenConfig.getEntity().getBack().getTenant())) {
                    return;
                }
            }
            throw new ServiceException("未在业务表中发现多租户关键字，请关闭多租户模式重试！");
        }
    }

    /**
     * 校验单表配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBase(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MODULE_ID.getCode())))
            throw new ServiceException("归属模块不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MENU_ID.getCode())))
            throw new ServiceException("归属菜单不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.ID.getCode())))
            throw new ServiceException("主键字段不能为空");
        for (GenTableColumnDto column : genTable.getSubList())
            if (column.isPk() && !ObjectUtil.equals(column.getId(), optionsObj.getLong(OptionField.ID.getCode())))
                throw new ServiceException("主键字段只能为数据表主键");
    }

    /**
     * 校验树表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclTree(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.TREE_ID.getCode())))
            throw new ServiceException("树编码字段不能为空");
        else if (StringUtils.isEmpty(optionsObj.getString(OptionField.PARENT_ID.getCode())))
            throw new ServiceException("树父编码字段不能为空");
        else if (StringUtils.isEmpty(optionsObj.getString(OptionField.TREE_NAME.getCode())))
            throw new ServiceException("树名称字段不能为空");
        else if (StringUtils.isEmpty(optionsObj.getString(OptionField.ANCESTORS.getCode())))
            throw new ServiceException("树祖籍列表字段不能为空");
    }

    /**
     * 校验主子表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclSub(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.FOREIGN_ID.getCode())))
            throw new ServiceException("外键关联的主表字段不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.SUB_TABLE_ID.getCode())))
            throw new ServiceException("关联子表的表名字段不能为空");
        else if (StringUtils.isEmpty(optionsObj.getString(OptionField.SUB_FOREIGN_ID.getCode())))
            throw new ServiceException("关联子表的外键名字段不能为空");
    }

    /**
     * 初始化代码生成表数据
     *
     * @param id Id
     * @return 业务表对象
     */
    private GenTableDto initTable(Long id) {
        GenTableDto table = baseManager.selectByIdExtra(id);
        JSONObject optionsObj = JSONObject.parseObject(table.getOptions());
        // 设置列信息
        switch (Objects.requireNonNull(TemplateType.getValue(table.getTplCategory()))) {
            case SUB_BASE:
                setSubTable(table, optionsObj);
                setBaseTable(table, optionsObj);
                break;
            case SUB_TREE:
                setSubTable(table, optionsObj);
            case TREE:
                setTreeTable(table, optionsObj);
            case BASE:
                setBaseTable(table, optionsObj);
        }
        setMenuOptions(table, optionsObj);
        return table;
    }

    /**
     * 设置基础表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setBaseTable(GenTableDto table, JSONObject optionsObj) {
        table.getSubList().forEach(column -> {
            if (column.isPk())
                table.setPkColumn(column);
        });
    }

    /**
     * 设置树表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setTreeTable(GenTableDto table, JSONObject optionsObj) {
    }

    /**
     * 设置主子表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setSubTable(GenTableDto table, JSONObject optionsObj) {
        table.setSubTable(baseManager.selectByIdExtra(optionsObj.getLong(OptionField.SUB_TABLE_ID.getCode())));
        JSONObject subOptionsObj = JSONObject.parseObject(table.getSubTable().getOptions());
        setBaseTable(table.getSubTable(), subOptionsObj);
        if (StrUtil.equalsAny(table.getSubTable().getTplCategory()
                , TemplateType.TREE.getCode(), TemplateType.SUB_TREE.getCode()))
            setTreeTable(table, optionsObj);
    }

    /**
     * 设置菜单信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setMenuOptions(GenTableDto table, JSONObject optionsObj) {
        Long menuId = optionsObj.getLong(OptionField.PARENT_MENU_ID.getCode());
        R<SysMenuDto> result = remoteMenuService.getInfoInner(menuId, SecurityConstants.INNER);
        if (result.isFail())
            throw new ServiceException("菜单服务异常，请联系管理员！");
        optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), result.getResult().getAncestors() + "," + result.getResult().getId());
        table.setOptions(optionsObj.toString());
    }

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<GenTableColumnDto> columnList, GenTableColumnDto column, GenTableDto table, Serializable id) {
        Long tableId = ObjectUtil.isNotNull(table) ? table.getId() : (Long) id;
        if (ObjectUtil.isNotNull(column))
            column.setTableId(tableId);
        else
            columnList.forEach(sub -> sub.setTableId(tableId));
    }
}
