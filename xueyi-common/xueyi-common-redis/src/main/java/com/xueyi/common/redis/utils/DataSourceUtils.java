package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 源策略缓存管理工具类
 *
 * @author xueyi
 */
public class DataSourceUtils {

    /** 初始化 redisService */
    private final static RedisService redisService = SpringUtils.getBean(RedisService.class);

    /**
     * 获取策略Id key
     *
     * @param strategyId 源策略Id
     * @return 缓存键key
     */
    public static String getSourceCacheKey(Long strategyId) {
        return CacheConstants.DATA_SOURCE_KEY + strategyId;
    }

    /**
     * 获取源策略组cache目录 key
     *
     * @return 缓存键key
     */
    public static String getCacheFolderKey() {
        return CacheConstants.DATA_SOURCE_KEY + "*";
    }

    /**
     * 获取企业策略主源 key
     *
     * @param enterpriseId 企业Id
     * @return 策略主数据源
     */
    public static <T> String getMainSourceNameByEnterpriseId(Long enterpriseId) {
        T source = redisService.getCacheObject(getSourceCacheKey(redisService.getCacheObject(EnterpriseUtils.getStrategyCacheKey(enterpriseId))));
        return getMainSource(source);
    }

    /**
     * 根据策略Id获取策略主源 key
     *
     * @param strategyId 策略Id
     * @return 策略主数据源
     */
    public static <T> String getMainSourceNameByStrategyId(Long strategyId) {
        T source = redisService.getCacheObject(DataSourceUtils.getSourceCacheKey(strategyId));
        return getMainSource(source);
    }

    /**
     * 获取源策略组
     *
     * @return SourceList 源策略集合
     */
    public static <T> List<T> getSourceList() {
        Collection<String> keys = redisService.keys(getCacheFolderKey());
        List<T> list = new ArrayList<>();
        for (String key : keys) {
            list.add(redisService.getCacheObject(key));
        }
        return list;
    }

    /**
     * 根据企业Id获取源策略
     *
     * @param enterpriseId 企业Id
     * @return 源策略
     */
    public static <T> T getSourceByEnterpriseId(Long enterpriseId) {
        Long strategyId = redisService.getCacheObject(EnterpriseUtils.getStrategyCacheKey(enterpriseId));
        return redisService.getCacheObject(DataSourceUtils.getSourceCacheKey(strategyId));
    }

    /**
     * 新增源策略缓存 cache
     *
     * @param strategyId 策略Id
     * @param source     策略组
     */
    public static <T> void refreshSourceCache(Long strategyId, T source) {
        redisService.setCacheObject(getSourceCacheKey(strategyId), source);
    }

    /**
     * 删除指定源策略 cache
     *
     * @param strategyId 策略Id
     */
    public static void deleteSourceCache(Long strategyId) {
        redisService.deleteObject(getSourceCacheKey(strategyId));
    }

    /**
     * 删除指定源策略集合 cache
     *
     * @param strategyIds 策略Ids
     */
    public static <T> void deleteSourceCaches(List<Long> strategyIds) {
        for (Long Id : strategyIds) {
            T source = redisService.getCacheObject(getSourceCacheKey(Id));
            if (source != null) {
                try {
                    Field fileId = source.getClass().getSuperclass().getDeclaredField("isChange");
                    fileId.setAccessible(true);
                    if (StringUtils.equals(BaseConstants.Default.NO.getCode(), (CharSequence) fileId.get(source))) {
                        deleteSourceCache(Id);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> String getMainSource(T source) {
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
}