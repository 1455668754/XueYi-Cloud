package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.cache.domain.CacheInitVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysCacheInitMapper {
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
     * 根据排除列表查询列表外企业信息 | 主源
     *
     * @param enterpriseIds 排除集合 企业Ids
     * @param systemIds     排除集合 模块Ids
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectEnterpriseCacheListByExcludeIds(@Param("enterpriseIds") Set<Long> enterpriseIds, @Param("systemIds") Set<Long> systemIds);

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

    /**
     * 根据排除列表查询列表外角色信息 | 指定源
     *
     * @param enterpriseIds 排除集合 企业Ids
     * @param roleIds       排除集合 角色Ids
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheListByExcludeIds(@Param("enterpriseIds") Set<Long> enterpriseIds, @Param("roleIds") Set<Long> roleIds);

}
