package com.xueyi.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.xueyi.common.core.constant.SecurityConstants;
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

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
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

    public static Long getEnterpriseId() {
        return Convert.toLong(get(SecurityConstants.Details.ENTERPRISE_ID.getCode()), 0L);
    }

    public static void setEnterpriseId(String enterpriseId) {
        set(SecurityConstants.Details.ENTERPRISE_ID.getCode(), enterpriseId);
    }

    public static String getEnterpriseName() {
        return get(SecurityConstants.Details.ENTERPRISE_NAME.getCode());
    }

    public static void setEnterpriseName(String enterpriseName) {
        set(SecurityConstants.Details.ENTERPRISE_NAME.getCode(), enterpriseName);
    }

    public static String getIsLessor() {
        return get(SecurityConstants.Details.IS_LESSOR.getCode());
    }

    public static void setIsLessor(String isLessor) {
        set(SecurityConstants.Details.IS_LESSOR.getCode(), isLessor);
    }

    public static Long getUserId() {
        return Convert.toLong(get(SecurityConstants.Details.USER_ID.getCode()), 0L);
    }

    public static void setUserId(String userId) {
        set(SecurityConstants.Details.USER_ID.getCode(), userId);
    }

    public static String getUserName() {
        return get(SecurityConstants.Details.USER_NAME.getCode());
    }

    public static void setUserName(String userName) {
        set(SecurityConstants.Details.USER_NAME.getCode(), userName);
    }

    public static String getUserType() {
        return get(SecurityConstants.Details.USER_TYPE.getCode());
    }

    public static void setUserType(String userType) {
        set(SecurityConstants.Details.USER_TYPE.getCode(), userType);
    }

    public static String getUserKey() {
        return get(SecurityConstants.Details.USER_KEY.getCode());
    }

    public static void setUserKey(String userKey) {
        set(SecurityConstants.Details.USER_KEY.getCode(), userKey);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
