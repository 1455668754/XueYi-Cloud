package com.xueyi.common.core.constant;

/**
 * 权限相关通用常量
 *
 * @author xueyi
 */
public class SecurityConstants {

    /** 令牌自定义标识 */
    public static final String TOKEN_AUTHENTICATION = "Authorization";

    /** 令牌前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 企业Id字段 */
    public static final String DETAILS_ENTERPRISE_ID = "enterpriseId";

    /** 企业名称字段 */
    public static final String DETAILS_ENTERPRISE_NAME = "enterpriseName";

    /** 用户Id字段 */
    public static final String DETAILS_USER_ID = "userId";

    /** 用户账号字段 */
    public static final String DETAILS_USERNAME = "userName";

    /** 用户类型字段 */
    public static final String DETAILS_TYPE = "userType";

    /** 授权信息字段 */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /** 请求来源 */
    public static final String FROM_SOURCE = "from-source";

    /** 内部请求 */
    public static final String INNER = "inner";
}
