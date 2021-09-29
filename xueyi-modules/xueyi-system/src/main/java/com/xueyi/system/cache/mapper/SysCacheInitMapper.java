package com.xueyi.system.cache.mapper;

import com.xueyi.system.cache.domain.CacheInitVo;

import java.util.List;

/**
 * 缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysCacheInitMapper {

    /**
     * 查询所有菜单信息 | 用于缓存加载
     *
     * @return 企业对象集合
     */
    public List<CacheInitVo> mainSelectMenuCacheList();

    /**
     * 查询所有模块信息 | 用于缓存加载
     *
     * @return 企业对象集合
     */
    public List<CacheInitVo> mainSelectSystemCacheList();
}
