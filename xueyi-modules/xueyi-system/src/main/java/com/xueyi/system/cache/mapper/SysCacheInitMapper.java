package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.authority.SysRole;
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
     * 查询所有模块信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemCacheList();

    /**
     * 查询所有模块-菜单信息 | 用于缓存加载
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemMenuCacheList();

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
