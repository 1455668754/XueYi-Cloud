package com.xueyi.system.cache.service;

import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;

/**
 * 缓存加载 业务层
 *
 * @author xueyi
 */
public interface ISysCacheInitService {

    /**
     * 加载指定企业全部缓存数据 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void loadingEnterpriseAllCacheByEnterpriseId(Long enterpriseId);

    /**
     * 加载企业缓存数据 | 主源所有企业
     */
    public void loadingEnterpriseCache();

    /**
     * 加载指定企业缓存数据 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void refreshEnterpriseCacheByEnterpriseId(Long enterpriseId);

    /**
     * 加载数据源策略组缓存数据 | 主源所有数据源策略组
     */
    public void loadingSourceCache();

    /**
     * 加载数据源策略组缓存数据 | 主源单个指定数据源策略组
     *
     * @param strategyId 源策略组Id
     */
    public void refreshSourceCacheByStrategyId(Long strategyId);

    /**
     * 加载模块-路由缓存数据 | 主源所有企业
     */
    public void loadingRouteCache();

    /**
     * 查询指定企业模块-路由信息 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void refreshRouteCacheByEnterpriseId(Long enterpriseId);

    /**
     * 加载模块-路由缓存数据 | 单个企业的单个指定模块
     *
     * @param system 模块信息 | systemId 模块Id | enterpriseId 企业Id
     */
    public void refreshRouteCacheBySystemId(SysSystem system);

    /**
     * 加载菜单缓存数据 | 主源所有企业
     */
    public void loadingMenuCache();

    /**
     * 加载菜单缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void refreshMenuCacheByEnterpriseId(Long enterpriseId);

    /**
     * 加载模块缓存数据 | 主源所有企业
     */
    public void loadingSystemCache();

    /**
     * 加载模块缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void refreshSystemCacheByEnterpriseId(Long enterpriseId);

    /**
     * 加载模块-菜单缓存数据 | 主源所有企业
     */
    public void loadingSystemMenuCache();

    /**
     * 加载模块-菜单缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    public void refreshSystemMenuCacheByEnterpriseId(Long enterpriseId);
}