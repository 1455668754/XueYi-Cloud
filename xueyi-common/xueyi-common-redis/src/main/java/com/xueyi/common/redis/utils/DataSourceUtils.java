package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;

import java.util.List;

public class DataSourceUtils {

    /**
     * 获取策略Id key
     *
     * @param strategyId 源策略Id
     * @return 缓存键key
     */
    public static String getSourceCacheKey(Long strategyId) {
        return Constants.DATA_SOURCE_KEY + strategyId;
    }

    /**
     * 获取源策略组cache目录 key
     *
     * @return 缓存键key
     */
    public static String getCacheFolderKey() {
        return Constants.DATA_SOURCE_KEY + ":*";
    }

    /**
     * 新增源策略缓存 cache
     *
     * @param strategyId 策略Id
     * @param source     策略组
     */
    public static <T> void refreshCache(Long strategyId, T source) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setCacheObject(getSourceCacheKey(strategyId), source);
    }

    /**
     * 删除指定源策略 cache
     *
     * @param strategyId 策略Id
     */
    public static void deleteCache(Long strategyId) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getSourceCacheKey(strategyId));
    }

    /**
     * 删除指定源策略集合 cache
     *
     * @param strategyIds 策略Ids
     */
    public static <T> void deleteCaches(List<Long> strategyIds) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
            for (int i = 0; i < strategyIds.size(); i++) {
                Long Id = strategyIds.get(i);
            T source = redisService.getCacheObject(getSourceCacheKey(Id));
            try {
                System.out.println(source.getClass().getField("isChange"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除所有源策略 cache目录
     */
    public static void deleteCacheFolder() {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.deleteObject(getCacheFolderKey());
    }
}