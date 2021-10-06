package com.xueyi.system.cache.service;

import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;

/**
 * 缓存加载 业务层
 *
 * @author xueyi
 */
public interface ISysCacheInitService {

    /**
     * 加载模块-路由缓存数据
     */
    public void loadingRouteCache();

    /**
     * 根据模块信息查询模块-路由信息
     *
     * @param system 模块信息
     */
    public void refreshRouteCacheBySystem(SysSystem system);

    /**
     * 加载菜单缓存数据
     */
    public void loadingMenuCache();

    /**
     * 根据菜单信息查询菜单信息
     *
     * @param menu 菜单信息
     */
    public void refreshMenuCacheByMenu(SysMenu menu);

    /**
     * 加载模块缓存数据
     */
    public void loadingSystemCache();

    /**
     * 根据模块信息查询模块信息
     *
     * @param system 模块信息
     */
    public void refreshSystemCacheBySystem(SysSystem system);

    /**
     * 加载模块-菜单缓存数据
     */
    public void loadingSystemMenuCache();

    /**
     * 根据模块信息查询模块-菜单信息
     *
     * @param system 模块信息
     */
    public void refreshSystemMenuCacheBySystem(SysSystem system);

    /**
     * 加载角色缓存数据
     *
     * @param sourceName 数据源名称
     */
    public void loadingRoleCache(String sourceName);

    /**
     * 根据角色信息更新角色缓存
     *
     * @param role       角色信息 | roleId 角色Id | enterpriseId 租户Id
     * @param sourceName 指定源
     */
    public void refreshRoleCacheByRoleIdToSourceName(SysRole role, String sourceName);

    /**
     * 根据角色信息更新角色缓存
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public void refreshRoleCacheByRoleId(SysRole role);
}