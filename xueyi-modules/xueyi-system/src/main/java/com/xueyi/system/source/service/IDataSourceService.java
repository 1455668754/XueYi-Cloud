package com.xueyi.system.source.service;

/**
 * 数据源信息 业务层
 *
 * @author xueyi
 */
public interface IDataSourceService {

    /**
     * 加载数据源策略组缓存数据
     */
    public void loadingSourceCache();

    /**
     * 根据策略Id刷新策略缓存 cache
     */
    public void mainRefreshSource(Long strategyId);
}
