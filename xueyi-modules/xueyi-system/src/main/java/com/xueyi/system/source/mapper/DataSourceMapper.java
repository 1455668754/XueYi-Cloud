package com.xueyi.system.source.mapper;

import com.xueyi.system.api.source.Source;

import java.util.List;

/**
 * 数据源信息 数据层
 *
 * @author xueyi
 */
public interface DataSourceMapper {
    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    public List<Source> selectLoadDataSources(Source source);
}