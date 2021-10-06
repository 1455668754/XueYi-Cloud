package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.cache.domain.CacheInitVo;

import java.util.List;

/**
 * 缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysCacheInitMapper {

    /**
     * 查询所有模块-路由信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectRouteCacheList();

    /**
     * 根据模块信息查询模块-路由信息
     *
     * @param system 模块信息
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectRouteCacheListBySystem(SysSystem system);

    /**
     * 查询所有菜单信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectMenuCacheList();

    /**
     * 根据菜单信息查询菜单信息
     *
     * @param enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectMenuCacheListByEnterpriseId(Long enterpriseId);
    /**
     * 查询所有模块信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemCacheList();

    /**
     * 根据模块信息查询模块信息
     *
     * @param system 模块信息
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemCacheListBySystem(SysSystem system);

    /**
     * 查询所有模块-菜单信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemMenuCacheList();

    /**
     * 根据模块信息查询模块-菜单信息
     *
     * @param system 模块信息
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemMenuCacheListBySystem(SysSystem system);

    /**
     * 查询所有角色信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheList();

    /**
     * 根据角色信息查找角色信息 | 用于更新单个角色缓存
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public SysRole selectRoleCacheByRoleId(SysRole role);
}
