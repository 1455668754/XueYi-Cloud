package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.source.Source;

import java.util.List;

/**
 * 源组缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysSourceCacheMapper {

    /**
     * 查询所有数据源策略组信息 | 主源所有数据源策略组
     *
     * @return 数据源组集合
     */
    public List<Source> mainSelectSourceCacheListBySource();

    /**
     * 根据源策略Id查询数据源策略组信息 | 主源单个指定数据源策略组
     *
     * @param strategyId 源策略组Id
     * @return 数据源组
     */
    public Source mainSelectSourceCacheByStrategyId(Long strategyId);
}