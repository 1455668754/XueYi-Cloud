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

    /**
     * 加载角色缓存数据 | 指定源所有企业
     *
     * @param sourceName 数据源名称
     */
    public void loadingRoleCache(String sourceName);

    /**
     * 加载角色缓存数据 | 单个企业的单个指定角色
     *
     * @param role       角色信息 | roleId 角色Id | enterpriseId 租户Id
     * @param sourceName 指定源
     */
    public void refreshRoleCacheByRoleIdToSourceName(SysRole role, String sourceName);

    /**
     * 加载角色缓存数据 | 单个企业的单个指定角色
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public void refreshRoleCacheByRoleIdToIsolate(SysRole role);
}