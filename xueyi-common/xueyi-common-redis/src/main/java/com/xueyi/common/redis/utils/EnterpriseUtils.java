package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;

/**
 * 企业缓存管理工具类
 *
 * @author xueyi
 */
public class EnterpriseUtils {

    /**
     * 获取企业Id key
     *
     * @param enterpriseName 企业账号
     * @return 缓存键key
     */
    public static String getLoginCacheKey(String enterpriseName) {
        return Constants.LOGIN_ENTERPRISE_KEY + enterpriseName;
    }

    /**
     * 获取企业信息 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getEnterpriseCacheKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.ENTERPRISE_KEY;
    }

    /**
     * 获取企业cache目录 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getCacheFolderKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":*";
    }

    /**
     * 获取源策略Id key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getStrategyCacheKey(Long enterpriseId) {
        return Constants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + Constants.STRATEGY_KEY;
    }

    /**
     * 新增企业信息缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param enterprise   企业信息
     */
    public static <T> void refreshEnterpriseCache(Long enterpriseId, T enterprise) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getEnterpriseCacheKey(enterpriseId), enterprise);
    }

    /**
     * 删除指定企业信息 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteEnterpriseCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getEnterpriseCacheKey(enterpriseId));
    }

    /**
     * 新增企业登录信息缓存 cache
     *
     * @param enterpriseName 企业账号
     * @param enterpriseId   企业Id
     */
    public static void refreshLoginCache(String enterpriseName, Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getLoginCacheKey(enterpriseName), enterpriseId);
    }

    /**
     * 删除指定企业登录信息 cache
     *
     * @param enterpriseName 企业账号
     */
    public static void deleteLoginCache(String enterpriseName) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getLoginCacheKey(enterpriseName));
    }

    /**
     * 新增企业策略信息缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param strategyId   策略Id
     */
    public static void refreshStrategyCache(Long enterpriseId, Long strategyId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getStrategyCacheKey(enterpriseId), strategyId);
    }

    /**
     * 删除指定企业策略信息 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteStrategyCache(Long enterpriseId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getStrategyCacheKey(enterpriseId));
    }

    /**
     * 删除指定租户缓存 cache目录
     *
     * @param enterpriseId 企业Id
     * @param enterpriseName 企业账号
     */
    public static void deleteCacheFolder(Long enterpriseId, String enterpriseName) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getCacheFolderKey(enterpriseId));
        redisService.deleteObject(getLoginCacheKey(enterpriseName));
    }
}