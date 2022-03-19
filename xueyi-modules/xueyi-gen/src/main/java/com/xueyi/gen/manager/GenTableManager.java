package com.xueyi.gen.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.SubBaseManager;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.mapper.GenTableColumnMapper;
import com.xueyi.gen.mapper.GenTableMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 业务管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class GenTableManager extends SubBaseManager<GenTableDto, GenTableMapper, GenTableColumnDto, GenTableColumnMapper> {

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    public List<GenTableDto> selectDbTableList(GenTableDto genTableDto) {
        return baseMapper.selectDbTableList(genTableDto);
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    public List<GenTableDto> selectDbTableListByNames(String[] names) {
        return baseMapper.selectDbTableListByNames(names);
    }

    /**
     * 根据表名称查询数据库列表
     *
     * @param name 表名称
     * @return 数据库表
     */
    public GenTableDto selectDbTableByName(String name) {
        return baseMapper.selectDbTableByName(name);
    }

    /**
     * 修改其它生成选项
     *
     * @param id     Id
     * @param options 其它生成选项
     * @return 结果
     */
    public int updateOptions(Serializable id, String options) {
        return baseMapper.update(new GenTableDto(),
                Wrappers.<GenTableDto>update().lambda()
                        .set(GenTableDto::getOptions, options)
                        .eq(GenTableDto::getId, id));
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<GenTableColumnDto> queryWrapper, LambdaUpdateWrapper<GenTableColumnDto> updateWrapper, GenTableDto table, Serializable id) {
        Serializable Id = ObjectUtil.isNotNull(table) ? table.getId() : id;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(GenTableColumnDto::getTableId, Id);
        else
            updateWrapper.eq(GenTableColumnDto::getTableId, Id);
    }
}
