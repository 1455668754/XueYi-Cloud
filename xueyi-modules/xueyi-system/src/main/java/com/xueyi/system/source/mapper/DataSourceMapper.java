package com.xueyi.system.source.mapper;

import com.xueyi.system.api.domain.source.Source;

import java.util.List;

/**
 * 数据源信息 数据层
 *
 * @author xueyi
 */
public interface DataSourceMapper {

    /**
     * 查询所有数据源策略信息 | 用于缓存加载
     *
     * @return 数据源组集合
     */
    public List<Source> mainSelectDataSourceCacheList();

    /**
     * 根据策略Id查询源策略信息
     *
     * @return 源策略信息
     */
    public Source mainSelectDataSourceCacheByStrategyId(Long strategyId);

    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    public List<Source> selectLoadDataSources(Source source);
}