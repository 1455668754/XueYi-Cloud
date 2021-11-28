package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限缓存管理工具类
 *
 * @author xueyi
 */
public class AuthorityUtils {

    /** 初始化 redisService */
    private final static RedisService redisService = SpringUtils.getBean(RedisService.class);

    /**
     * 获取模块-路由 key
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     * @return 缓存键key
     */
    public static String getRouteCacheKey(Long enterpriseId, Long systemId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.ROUTE_KEY + ":" + systemId;
    }

    /**
     * 获取菜单 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getMenuCacheKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.MENU_KEY;
    }

    /**
     * 获取模块 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getSystemCacheKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.SYSTEM_KEY;
    }

    /**
     * 获取模块-菜单 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getSystemMenuCacheKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.SYSTEM_MENU_KEY;
    }

    /**
     * 获取模块-路由目录 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getRouteCacheFolderKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.ROUTE_KEY + ":*";
    }

    /**
     * 获取模块-路由缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     */
    public static <T> Set<T> getRouteCache(Long enterpriseId, Long systemId) {
        Set<T> privateSet = redisService.getCacheObject(getRouteCacheKey(enterpriseId, systemId));
        Set<T> commonSet = redisService.getCacheObject(getRouteCacheKey(AuthorityConstants.COMMON_ENTERPRISE, systemId));
        return assemble(privateSet, commonSet);
    }

    /**
     * 新增模块-路由缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     * @param routeSet     模块-路由集合
     */
    public static <T> void refreshRouteCache(Long enterpriseId, Long systemId, Set<T> routeSet) {
        redisService.setCacheObject(getRouteCacheKey(enterpriseId, systemId), routeSet);
    }

    /**
     * 删除指定模块-路由 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteRouteCache(Long enterpriseId, Long systemId) {
        redisService.deleteObject(getRouteCacheKey(enterpriseId, systemId));
    }

    /**
     * 删除指定企业模块-路由目录 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteRouteCacheFolder(Long enterpriseId) {
        Collection<String> keys = redisService.keys(getRouteCacheFolderKey(enterpriseId));
        redisService.deleteObject(keys);
    }

    /**
     * 获取菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getMenuCache(Long enterpriseId) {
        Set<T> privateSet = redisService.getCacheObject(getMenuCacheKey(enterpriseId));
        Set<T> commonSet = redisService.getCacheObject(getMenuCacheKey(AuthorityConstants.COMMON_ENTERPRISE));
        return assemble(privateSet, commonSet);
    }

    /**
     * 新增菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param menuSet      菜单集合
     */
    public static <T> void refreshMenuCache(Long enterpriseId, Set<T> menuSet) {
        redisService.setCacheObject(getMenuCacheKey(enterpriseId), menuSet);
    }

    /**
     * 删除指定菜单 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteMenuCache(Long enterpriseId) {
        redisService.deleteObject(getMenuCacheKey(enterpriseId));
    }

    /**
     * 获取模块缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getSystemCache(Long enterpriseId) {
        Set<T> privateSet = redisService.getCacheObject(getSystemCacheKey(enterpriseId));
        Set<T> commonSet = redisService.getCacheObject(getSystemCacheKey(AuthorityConstants.COMMON_ENTERPRISE));
        return assemble(privateSet, commonSet);
    }

    /**
     * 新增模块缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemSet    模块集合
     */
    public static <T> void refreshSystemCache(Long enterpriseId, Set<T> systemSet) {
        redisService.setCacheObject(getSystemCacheKey(enterpriseId), systemSet);
    }

    /**
     * 删除指定模块 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteSystemCache(Long enterpriseId) {
        redisService.deleteObject(getSystemCacheKey(enterpriseId));
    }

    /**
     * 获取模块-菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getSystemMenuCache(Long enterpriseId) {
        Set<T> privateSet = redisService.getCacheObject(getSystemMenuCacheKey(enterpriseId));
        Set<T> commonSet = redisService.getCacheObject(getSystemMenuCacheKey(AuthorityConstants.COMMON_ENTERPRISE));
        return assemble(privateSet, commonSet);
    }

    /**
     * 获取公共模块-菜单缓存 cache
     */
    public static <T> Set<T> getCommonSystemMenuCache() {
        Set<T> commonSet = redisService.getCacheObject(getSystemMenuCacheKey(AuthorityConstants.COMMON_ENTERPRISE));
        return assemble(null, commonSet);
    }

    /**
     * 获取私有模块-菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getPrivateSystemMenuCache(Long enterpriseId) {
        Set<T> privateSet = redisService.getCacheObject(getSystemMenuCacheKey(enterpriseId));
        return assemble(privateSet, null);
    }

    /**
     * 新增模块-菜单缓存 cache
     *
     * @param enterpriseId  企业Id
     * @param systemMenuSet 模块-菜单集合
     */
    public static <T> void refreshSystemMenuCache(Long enterpriseId, Set<T> systemMenuSet) {
        redisService.setCacheObject(getSystemMenuCacheKey(enterpriseId), systemMenuSet);
    }

    /**
     * 删除指定模块-菜单 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteSystemMenuCache(Long enterpriseId) {
        redisService.deleteObject(getSystemMenuCacheKey(enterpriseId));
    }

    /**
     * 拼接公共与私有数据 cache
     *
     * @param privateSet 私有数据
     * @param commonSet  公共数据
     */
    private static <T> Set<T> assemble(Set<T> privateSet, Set<T> commonSet) {
        if (privateSet != null && commonSet != null) {
            privateSet.addAll(commonSet);
            return privateSet;
        }
        return privateSet == null && commonSet == null ? new HashSet<>() : privateSet == null ? commonSet : privateSet;
    }
}
