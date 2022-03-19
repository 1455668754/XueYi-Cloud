package com.xueyi.common.core.context;

import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.text.Convert;
import com.xueyi.common.core.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 企业Id | 企业账号 | 租户类型 | 用户id | 用户账号 | 用户类型 | Token等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * @author xueyi
 */
public class SecurityContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取企业Id
     */
    public static Long getEnterpriseId() {
        return Convert.toLong(get(SecurityConstants.ENTERPRISE_ID), SecurityConstants.EMPTY_TENANT_ID);
    }

    /**
     * 设置企业Id
     */
    public static void setEnterpriseId(String enterpriseId) {
        set(SecurityConstants.ENTERPRISE_ID, enterpriseId);
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName() {
        return get(SecurityConstants.ENTERPRISE_NAME);
    }

    /**
     * 设置企业名称
     */
    public static void setEnterpriseName(String enterpriseName) {
        set(SecurityConstants.ENTERPRISE_NAME, enterpriseName);
    }

    /**
     * 获取租户权限标识
     */
    public static String getIsLessor() {
        return get(SecurityConstants.IS_LESSOR);
    }

    /**
     * 设置租户权限标识
     */
    public static void setIsLessor(String isLessor) {
        set(SecurityConstants.IS_LESSOR, isLessor);
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return Convert.toLong(get(SecurityConstants.USER_ID), SecurityConstants.EMPTY_USER_ID);
    }

    /**
     * 设置用户Id
     */
    public static void setUserId(String userId) {
        set(SecurityConstants.USER_ID, userId);
    }

    /**
     * 获取用户名称
     */
    public static String getUserName() {
        return get(SecurityConstants.USER_NAME);
    }

    /**
     * 设置用户名称
     */
    public static void setUserName(String userName) {
        set(SecurityConstants.USER_NAME, userName);
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        return get(SecurityConstants.USER_TYPE);
    }

    /**
     * 设置用户权限标识
     */
    public static void setUserType(String userType) {
        set(SecurityConstants.USER_TYPE, userType);
    }

    /**
     * 获取租户策略源
     */
    public static String getSourceName() {
        return get(SecurityConstants.SOURCE_NAME);
    }

    /**
     * 设置租户策略源
     */
    public static void setSourceName(String sourceName) {
        set(SecurityConstants.SOURCE_NAME, sourceName);
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return get(SecurityConstants.USER_KEY);
    }

    /**
     * 设置用户key
     */
    public static void setUserKey(String userKey) {
        set(SecurityConstants.USER_KEY, userKey);
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StrUtil.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
