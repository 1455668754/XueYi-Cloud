package com.xueyi.common.security.utils;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.system.api.source.domain.Source;

import java.io.Serializable;

/**
 * 源策略组缓存管理工具类
 *
 * @author xueyi
 */
public class SourceUtils {

    /**
     * 源策略组查询
     *
     * @param Id 源策略组Id
     */
    public static Source getSourceCache(Serializable Id) {
        return SpringUtils.getBean(RedisService.class).getCacheMapValue(CacheConstants.DATA_SOURCE_KEY, Id.toString());
    }
}