package com.xueyi.common.redis.utils;

import com.xueyi.common.core.constant.Constants;

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
}