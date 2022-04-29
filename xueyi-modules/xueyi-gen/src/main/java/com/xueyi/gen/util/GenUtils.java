package com.xueyi.gen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.gen.GenConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 代码生成器 工具类
 *
 * @author xueyi
 */
public class GenUtils {

    /**
     * 初始化表信息
     */
    public static void initTable(GenTableDto genTable) {
        genTable.setClassName(StringUtils.convertToCamelCase(genTable.getName()));
        getRemoveItem(genTable);
        genTable.setBusinessName(getBusinessName(genTable.getName()));
        genTable.setFunctionName(replaceText(genTable.getComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        if (StrUtil.isNotBlank(GenConfig.getUiPath()))
            genTable.setUiPath(GenConfig.getUiPath());
    }

    /**
     * 初始化其它生成选项
     */
    public static void initTableOptions(List<GenTableColumnDto> columnList, GenTableDto table) {
        JSONObject optionJson = new JSONObject();
        Object[] javaFields = columnList.stream().map(GenTableColumnDto::getJavaField).toArray();
        // 设置默认模块
        optionJson.put(GenConstants.OptionField.PARENT_MODULE_ID.getCode(), AuthorityConstants.MODULE_DEFAULT_NODE.toString());
        // 设置默认菜单
        optionJson.put(GenConstants.OptionField.PARENT_MENU_ID.getCode(), AuthorityConstants.MENU_TOP_NODE.toString());
        // 检测是否为多租户模式
        optionJson.put(GenConstants.OptionField.IS_TENANT.getCode(),
                arraysContains(GenConfig.getEntity().getBack().getTenant(), javaFields)
                        ? DictConstants.DicYesNo.YES.getCode()
                        : DictConstants.DicYesNo.NO.getCode());
        // 设置默认源策略模式
        optionJson.put(GenConstants.OptionField.SOURCE_MODE.getCode(),
                StrUtil.equals(optionJson.getString(GenConstants.OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode())
                        ? GenConstants.SourceMode.ISOLATE.getCode()
                        : GenConstants.SourceMode.MASTER.getCode());
        columnList.forEach(column -> {
            if (column.isPk()
                    && StrUtil.equals(column.getJavaField(), GenConstants.OptionField.ID.getCode())) {
                optionJson.put(GenConstants.OptionField.ID.getCode(), column.getId().toString());
                optionJson.put(GenConstants.OptionField.TREE_ID.getCode(), column.getId().toString());
                optionJson.put(GenConstants.OptionField.FOREIGN_ID.getCode(), column.getId().toString());
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.NAME.getCode())) {
                optionJson.put(GenConstants.OptionField.NAME.getCode(), column.getId().toString());
                optionJson.put(GenConstants.OptionField.TREE_NAME.getCode(), column.getId().toString());
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.STATUS.getCode())) {
                optionJson.put(GenConstants.OptionField.STATUS.getCode(), column.getId().toString());
                optionJson.put(GenConstants.OptionField.API_ES.getCode(), DictConstants.DicYesNo.YES.getCode());
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.SORT.getCode())) {
                optionJson.put(GenConstants.OptionField.SORT.getCode(), column.getId().toString());
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.PARENT_ID.getCode())) {
                optionJson.put(GenConstants.OptionField.PARENT_ID.getCode(), column.getId().toString());
                table.setTplCategory(GenConstants.TemplateType.TREE.getCode());
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.ANCESTORS.getCode())) {
                optionJson.put(GenConstants.OptionField.ANCESTORS.getCode(), column.getId().toString());
            }
        });

        optionJson.put(GenConstants.OptionField.API_LIST.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_GET_INFO.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_ADD.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_EDIT.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_BATCH_REMOVE.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_EXPORT.getCode(), DictConstants.DicYesNo.YES.getCode());
        table.setOptions(optionJson.toString());
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumnDto column, GenTableDto table) {
        String dataType = column.getType();
        column.setTableId(table.getId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(column.getName()));
        String javaField = column.getJavaField();
        // 设置默认类型
        column.setJavaType(GenConstants.JavaType.STRING.getCode());
        // 设置默认显示类型
        column.setHtmlType(GenConstants.DisplayType.INPUT.getCode());
        // 设置默认查询类型（长整型防精度丢失，到前端会自动转成字符串，故使用文本框）
        column.setQueryType(GenConstants.QueryType.EQ.getCode());

        if (arraysContains(GenConfig.getDataBase().getTypeStr(), dataType) && !StrUtil.equals(column.getComment(), column.readNameNoSuffix())) {
            column.setHtmlType(GenConstants.DisplayType.SELECT.getCode());
            if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.NORMAL_DISABLE.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_NORMAL_DISABLE.getCode());
            } else if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.SHOW_HIDE.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_SHOW_HIDE.getCode());
            } else if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.YES_NO.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_YES_NO.getCode());
            }
        } else if (arraysContains(GenConfig.getDataBase().getTypeText(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_TEXTAREA.getCode());
        } else if (arraysContains(GenConfig.getDataBase().getTypeDate(), dataType)) {
            column.setJavaType(GenConstants.JavaType.DATE.getCode());
            column.setHtmlType(GenConstants.DisplayType.DATE_PICKER.getCode());
        } else if (arraysContains(GenConfig.getDataBase().getTypeFloat(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.BIG_DECIMAL.getCode());
        } else if (arraysContains(GenConfig.getDataBase().getTypeNumber(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.INTEGER.getCode());
        } else if (arraysContains(GenConfig.getDataBase().getTypeLong(), dataType)) {
            column.setJavaType(GenConstants.JavaType.LONG.getCode());
        }

        // 插入字段（默认除必须移除的参数外所有字段都需要插入）
        boolean mustCheck = arraysContains(GenConfig.getEntity().getMustHide(), javaField) || column.isPk();
        column.setInsert(!(arraysContains(GenConfig.getOperate().getNotInsert(), javaField) || mustCheck));
        // 查看字段
        column.setView(!(arraysContains(GenConfig.getOperate().getNotView(), javaField) || mustCheck));
        // 编辑字段
        column.setEdit(!(arraysContains(GenConfig.getOperate().getNotEdit(), javaField) || mustCheck));
        // 列表字段
        column.setList(!(arraysContains(GenConfig.getOperate().getNotList(), javaField) || mustCheck));
        // 查询字段
        column.setQuery(!(arraysContains(GenConfig.getOperate().getNotQuery(), javaField) || mustCheck));
        // 导入字段
        column.setImport(!(arraysContains(GenConfig.getOperate().getNotImport(), javaField) || mustCheck));
        // 导出字段
        column.setExport(!(arraysContains(GenConfig.getOperate().getNotExport(), javaField) || mustCheck));
        // 隐藏字段
        column.setHide(arraysContains(GenConfig.getEntity().getMustHide(), javaField));
        // 覆盖字段（默认无需覆盖字段）
        column.setCover(false);

        // 特殊指定
        GenConstants.GenField field = GenConstants.GenField.getValue(javaField);
        if (ObjectUtil.isNotNull(field)) {
            switch (Objects.requireNonNull(field)) {
                case NAME:
                    column.setQueryType(GenConstants.QueryType.LIKE.getCode());
                    break;
                case SEX:
                    column.setDictType(DictConstants.DictType.SYS_USER_SEX.getCode());
                    break;
                case LOGO:
                case IMAGE:
                    column.setHtmlType(GenConstants.DisplayType.IMAGE_UPLOAD.getCode());
                    break;
                case FILE:
                    column.setHtmlType(GenConstants.DisplayType.FILE_UPLOAD.getCode());
                    break;
                case COMMENT:
                    column.setHtmlType(GenConstants.DisplayType.TINYMCE.getCode());
                    break;
                case REMARK:
                    column.setHtmlType(GenConstants.DisplayType.INPUT_TEXTAREA.getCode());
                    break;
            }
        }
        // 最终校验
        basicInitColumn(column);
    }

    /**
     * 最终校验列属性字段
     */
    public static void updateCheckColumn(GenTableDto table) {
        JSONObject objectJson = JSONObject.parseObject(table.getOptions());
        table.getSubList().forEach(column -> {
            if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.ID.getCode()))) {
                column.setJavaField(GenConstants.OptionField.ID.getCode());
            } else if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.NAME.getCode()))) {
                column.setJavaField(GenConstants.OptionField.NAME.getCode());
            } else if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.STATUS.getCode()))) {
                column.setJavaField(GenConstants.OptionField.STATUS.getCode());
            } else if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.SORT.getCode()))) {
                column.setJavaField(GenConstants.OptionField.SORT.getCode());
            }
            if (StrUtil.equalsAny(table.getTplCategory(),
                    GenConstants.TemplateType.TREE.getCode(), GenConstants.TemplateType.SUB_TREE.getCode())) {
                if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.PARENT_ID.getCode()))) {
                    column.setJavaField(GenConstants.OptionField.PARENT_ID.getCode());
                } else if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.ANCESTORS.getCode()))) {
                    column.setJavaField(GenConstants.OptionField.ANCESTORS.getCode());
                }
            }
            // 最终校验
            basicInitColumn(column);
        });
    }

    /**
     * 最终校验列属性字段
     */
    public static void basicInitColumn(GenTableColumnDto column) {
        // 校验是否需要覆盖
        GenConstants.GenField field = GenConstants.GenField.getValue(column.getJavaField());
        if (ObjectUtil.isNotNull(field)) {
            switch (Objects.requireNonNull(field)) {
                case ID:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.LONG.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.ID.getKey()))) {
                        column.setCover(true);
                    }
                    break;
                case NAME:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.NAME.getKey())
                            && StrUtil.equals(column.getQueryType(), GenConstants.QueryType.LIKE.getCode()))) {
                        column.setCover(true);
                    }
                    break;
                case STATUS:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.STATUS.getKey())
                            && StrUtil.contains(column.getComment(), GenConstants.GenCheck.NORMAL_DISABLE.getInfo()))) {
                        column.setCover(true);
                    }
                    break;
                case SORT:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.INTEGER.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.SORT.getKey()))) {
                        column.setCover(true);
                    }
                    break;
                case PARENT_ID:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.LONG.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.PARENT_ID.getKey()))) {
                        column.setCover(true);
                    }
                    break;
                case ANCESTORS:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.ANCESTORS.getKey()))) {
                        column.setCover(true);
                    }
                    break;
                case REMARK:
                    if (!(StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode())
                            && StrUtil.equals(column.getName(), GenConstants.GenField.REMARK.getKey()))) {
                        column.setCover(true);
                    }
                    break;
                default:
                    column.setCover(false);
            }
        }
        // 校验是否需要隐藏
        boolean isMustHide = StrUtil.equalsAny(column.getName(), GenConfig.getEntity().getMustHide());
        if (column.isHide() || isMustHide) {
            if (isMustHide) {
                column.setHide(true);
                column.setCover(false);
            }
            column.setView(false);
            column.setInsert(false);
            column.setEdit(false);
            column.setImport(false);
            column.setExport(false);
            column.setUnique(false);
            column.setRequired(false);
            column.setList(false);
            column.setQuery(false);
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 校验数组中是否存在值在另一个数组中
     *
     * @param arr      数组
     * @param checkArr 查询数组
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, Object[] checkArr) {
        for (Object targetValue : checkArr) {
            if (arraysContains(arr, targetValue.toString()))
                return true;
        }
        return false;
    }

    /**
     * 获取符合的替换前缀组 | 获取包名 | 获取模块名
     *
     * @param genTable 业务表对象
     */
    public static void getRemoveItem(GenTableDto genTable) {
        if (GenConfig.isAutoRemovePre() && CollUtil.isNotEmpty(GenConfig.getRemoveLists())) {
            for (GenConfig.RemoveItem removeItem : GenConfig.getRemoveLists()) {
                if (StrUtil.equals(StrUtil.sub(genTable.getName(), 0, removeItem.getPrefix().length()), removeItem.getPrefix())) {
                    genTable.setPrefix(StringUtils.convertToCamelCase(removeItem.getPrefix()));
                    genTable.setPackageName(removeItem.getPackageName());
                    genTable.setModuleName(getModuleName(removeItem.getPackageName()));
                    genTable.setAuthorityName(genTable.getModuleName());
                    return;
                }
            }
        }
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(StrUtil.DOT);
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf(StrUtil.UNDERLINE);
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:信息表|表|雪忆)", StrUtil.EMPTY);
    }
}