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
     * 获取菜单 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getMenuCacheKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.MENU_KEY;
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
     * 新增菜单缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param menuSet      菜单集合
     */
    public static <T> void refreshMenuCache(Long enterpriseId, Set<T> menuSet) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getMenuCacheKey(enterpriseId), menuSet);
    }

    /**
     * 删除指定菜单 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteMenuCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getMenuCacheKey(enterpriseId));
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
}
