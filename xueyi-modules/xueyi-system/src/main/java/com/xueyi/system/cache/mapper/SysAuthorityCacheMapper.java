package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.cache.domain.CacheInitVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 权限缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysAuthorityCacheMapper {

    /**
     * 查询所有模块-路由信息 | 主源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectRouteCacheListBySource();

    /**
     * 查询指定企业模块-路由信息 | 指定企业
     *
     * @param enterpriseId 企业Id
     * @return 通用缓存封装对象集合
     */
    public List<CacheInitVo> mainSelectRouteCacheListByEnterpriseId(Long enterpriseId);

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
}
