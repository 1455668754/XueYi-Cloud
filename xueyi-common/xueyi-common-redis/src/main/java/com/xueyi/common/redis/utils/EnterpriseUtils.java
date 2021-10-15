package com.xueyi.common.redis.utils;

import cn.hutool.core.bean.BeanUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.lang.reflect.Field;

/**
 * 企业缓存管理工具类
 *
 * @author xueyi
 */
public class EnterpriseUtils {

    private static RedisService redisService = null;

    /**
     * 初始化 redisService
     */
    static {
        if(BeanUtil.isEmpty(redisService)){
            redisService = SpringUtils.getBean(RedisService.class);
        }
    }

    /**
     * 获取企业Id key
     *
     * @param enterpriseName 企业账号
     * @return 缓存键key
     */
    public static String getLoginCacheKey(String enterpriseName) {
        return CacheConstants.LOGIN_ENTERPRISE_KEY + enterpriseName;
    }

    /**
     * 获取企业信息 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getEnterpriseCacheKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.ENTERPRISE_KEY;
    }

    /**
     * 获取企业cache目录 key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getCacheFolderKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":*";
    }

    /**
     * 获取源策略Id key
     *
     * @param enterpriseId 企业Id
     * @return 缓存键key
     */
    public static String getStrategyCacheKey(Long enterpriseId) {
        return CacheConstants.SYS_ENTERPRISE_KEY + enterpriseId + ":" + CacheConstants.STRATEGY_KEY;
    }

    /**
     * 获取企业策略主源 key
     *
     * @param enterpriseId 企业Id
     * @return SourceName 策略主数据源
     */
    public static <T> String getMainSourceName(Long enterpriseId) {
        T source = redisService.getCacheObject(DataSourceUtils.getSourceCacheKey(redisService.getCacheObject(getStrategyCacheKey(enterpriseId))));
        if (source != null) {
            try {
                Field fileId = source.getClass().getDeclaredField("master");
                fileId.setAccessible(true);
                return (String) fileId.get(source);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 检测是否为租管租户 key
     *
     * @param enterpriseId 企业Id
     * @return 结果
     */
    public static <T> boolean isAdminTenant(Long enterpriseId) {
        T enterprise = redisService.getCacheObject(getEnterpriseCacheKey(enterpriseId));
        if (enterprise != null) {
            try {
                Field fileId = enterprise.getClass().getDeclaredField("isLessor");
                fileId.setAccessible(true);
                return StringUtils.equals(AuthorityConstants.TENANT_TYPE_ADMIN, (String) fileId.get(enterprise));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 新增企业信息缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param enterprise   企业信息
     */
    public static <T> void refreshEnterpriseCache(Long enterpriseId, T enterprise) {
        redisService.setCacheObject(getEnterpriseCacheKey(enterpriseId), enterprise);
    }

    /**
     * 删除指定企业信息 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteEnterpriseCache(Long enterpriseId) {
        redisService.deleteObject(getEnterpriseCacheKey(enterpriseId));
    }

    /**
     * 新增企业登录信息缓存 cache
     *
     * @param enterpriseName 企业账号
     * @param enterpriseId   企业Id
     */
    public static void refreshLoginCache(String enterpriseName, Long enterpriseId) {
        redisService.setCacheObject(getLoginCacheKey(enterpriseName), enterpriseId);
    }

    /**
     * 删除指定企业登录信息 cache
     *
     * @param enterpriseName 企业账号
     */
    public static void deleteLoginCache(String enterpriseName) {
        redisService.deleteObject(getLoginCacheKey(enterpriseName));
    }

    /**
     * 新增企业策略信息缓存 cache
     *
     * @param enterpriseId 企业Id
     * @param strategyId   策略Id
     */
    public static void refreshStrategyCache(Long enterpriseId, Long strategyId) {
        redisService.setCacheObject(getStrategyCacheKey(enterpriseId), strategyId);
    }

    /**
     * 删除指定企业策略信息 cache
     *
     * @param enterpriseId 企业Id
     */
    public static void deleteStrategyCache(Long enterpriseId) {
        redisService.deleteObject(getStrategyCacheKey(enterpriseId));
    }

    /**
     * 删除指定租户缓存 cache目录
     *
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业账号
     */
    public static void deleteCacheFolder(Long enterpriseId, String enterpriseName) {
        redisService.deleteObject(getCacheFolderKey(enterpriseId));
        redisService.deleteObject(getLoginCacheKey(enterpriseName));
    }
}