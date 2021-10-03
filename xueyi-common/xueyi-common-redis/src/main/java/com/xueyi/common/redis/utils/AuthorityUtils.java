package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
     * 获取模块-路由缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param systemId     模块Id
     */
    public static <T> Set<T> getRouteCache(Long enterpriseId, Long systemId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
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
     * 获取模块缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getSystemCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
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
     * 获取模块-菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     */
    public static <T> Set<T> getSystemMenuCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        Set<T> privateSet = redisService.getCacheObject(getSystemMenuCacheKey(enterpriseId));
        Set<T> commonSet = redisService.getCacheObject(getSystemMenuCacheKey(AuthorityConstants.COMMON_ENTERPRISE));
        return assemble(privateSet, commonSet);
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
     * 获取角色缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param roleIds      角色Id集合
     */
    public static <T> List<T> getRoleListCache(Long enterpriseId, Set<Long> roleIds) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        List<T> roleList = new ArrayList<>();
        if (!roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                T role = redisService.getCacheObject(getRoleCacheKey(enterpriseId, roleId));
                if (role != null) {
                    roleList.add(role);
                }
            }
        }
        return roleList;
    }

    /**
     * 获取角色缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param roleId       角色Id
     */
    public static <T> T getRoleCache(Long enterpriseId, Long roleId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getCacheObject(getRoleCacheKey(enterpriseId, roleId));
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
