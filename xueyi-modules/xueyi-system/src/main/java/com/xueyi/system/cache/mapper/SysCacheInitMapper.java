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
     * 查询所有模块-路由信息 | 主源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectRouteCacheListBySource();

    /**
     * 根据模块信息查询模块-路由信息 | 单个企业的单个指定模块
     *
     * @param system 模块信息 | systemId 模块Id | enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public CacheInitVo mainSelectRouteCacheListBySystemId(SysSystem system);

    /**
     * 查询所有菜单信息 | 主源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectMenuCacheListBySource();

    /**
     * 根据菜单信息查询菜单信息 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public CacheInitVo mainSelectMenuCacheListByEnterpriseId(Long enterpriseId);

    /**
     * 查询所有模块信息 | 主源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemCacheListBySource();

    /**
     * 根据模块信息查询模块信息 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public CacheInitVo mainSelectSystemCacheListByEnterpriseId(Long enterpriseId);

    /**
     * 查询所有模块-菜单信息 | 主源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectSystemMenuCacheListBySource();

    /**
     * 根据模块信息查询模块-菜单信息 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public CacheInitVo mainSelectSystemMenuCacheListByEnterpriseId(Long enterpriseId);

    /**
     * 查询所有角色信息 | 指定源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheListBySource();

    /**
     * 根据角色信息查找角色信息 | 单个企业的单个指定角色
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public SysRole selectRoleCacheByRoleId(SysRole role);
}
