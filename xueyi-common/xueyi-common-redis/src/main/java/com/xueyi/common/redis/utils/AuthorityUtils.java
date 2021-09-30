package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.util.Set;

/**
 * 权限缓存管理工具类
 *
 * @author xueyi
 */
public class AuthorityUtils {

    /**
     * 获取模块-路由 key
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     * @return 缓存键key
     */
    public static String getRouteCacheKey(Long enterpriseId, Long systemId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.ROUTE_KEY + ":" + systemId;
    }

    /**
     * 获取模块 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getSystemCacheKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.SYSTEM_KEY;
    }

    /**
     * 获取模块-菜单 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getSystemMenuCacheKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.SYSTEM_MENU_KEY;
    }

    /**
     * 获取角色 key
     *
     * @param enterpriseId 企业Id
     * @param roleId       角色Id
     * @return 缓存键key
     */
    public static String getRoleCacheKey(Long enterpriseId, Long roleId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.ROLE_KEY + ":" + roleId;
    }

    /**
     * 新增模块-路由缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     * @param routeSet     模块-路由集合
     */
    public static <T> void refreshRouteCache(Long enterpriseId, Long systemId, Set<T> routeSet) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getRouteCacheKey(enterpriseId, systemId), routeSet);
    }

    /**
     * 删除指定模块-路由 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteRouteCache(Long enterpriseId, Long systemId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getRouteCacheKey(enterpriseId, systemId));
    }

    /**
     * 新增模块缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemSet    模块集合
     */
    public static <T> void refreshSystemCache(Long enterpriseId, Set<T> systemSet) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getSystemCacheKey(enterpriseId), systemSet);
    }

    /**
     * 删除指定模块 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteSystemCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getSystemCacheKey(enterpriseId));
    }

    /**
     * 新增模块-菜单缓存 cache
     *
     * @param enterpriseId  企业Id
     * @param systemMenuSet 模块-菜单集合
     */
    public static <T> void refreshSystemMenuCache(Long enterpriseId, Set<T> systemMenuSet) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getSystemMenuCacheKey(enterpriseId), systemMenuSet);
    }

    /**
     * 删除指定模块-菜单 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteSystemMenuCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getSystemMenuCacheKey(enterpriseId));
    }

    /**
     * 新增角色缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param roleId       角色Id
     * @param role         角色信息
     */
    public static <T> void refreshRoleCache(Long enterpriseId, Long roleId, T role) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getRoleCacheKey(enterpriseId, roleId), role);
    }

    /**
     * 删除指定角色 cache
     *
     * @param enterpriseId 企业Id
     * @param roleId       角色Id
     */
    public static void deleteRoleCache(Long enterpriseId, Long roleId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getRoleCacheKey(enterpriseId, roleId));
    }
}
