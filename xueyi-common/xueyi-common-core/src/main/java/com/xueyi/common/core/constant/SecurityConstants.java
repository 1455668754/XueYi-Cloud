package com.xueyi.common.core.constant;

/**
 * 安全相关通用常量
 *
 * @author xueyi
 */
public class SecurityConstants {

    /** 请求来源 */
    public static final String FROM_SOURCE = "from-source";

    /** 内部请求 */
    public static final String INNER = "inner";

    /** 授权字段 */
    public enum Details {

        AUTHORIZATION_HEADER("authorization", "授权信息"),
        ENTERPRISE_ID("enterprise_id", "企业Id"),
        ENTERPRISE_NAME("enterprise_name", "企业账号"),
        IS_LESSOR("is_lessor", "企业类型"),
        USER_ID("user_id", "用户Id"),
        USER_NAME("user_name", "用户账号"),
        USER_TYPE("user_type", "用户类型"),
        SOURCE_NAME("source_name", "租户策略源"),
        USER_KEY("user_key", "用户标识"),
        LOGIN_USER("login_user", "登录用户");

        private final String code;
        private final String info;

        Details(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
