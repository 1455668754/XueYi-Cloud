package com.xueyi.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;

import java.util.List;

/**
 * 业务管理 数据层
 *
 * @author xueyi
 */
public interface GenTableMapper extends SubBaseMapper<GenTableDto, GenTableColumnDto> {

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    @InterceptorIgnore(tenantLine = "1")
    List<GenTableDto> selectDbTableList(GenTableDto genTableDto);

    /**
     * 根据表名称组查询数据库列表
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    @InterceptorIgnore(tenantLine = "1")
    List<GenTableDto> selectDbTableListByNames(String[] names);

    /**
     * 根据表名称查询数据库列表
     *
     * @param name 表名称
     * @return 数据库表
     */
    @InterceptorIgnore(tenantLine = "1")
    GenTableDto selectDbTableByName(String name);
}