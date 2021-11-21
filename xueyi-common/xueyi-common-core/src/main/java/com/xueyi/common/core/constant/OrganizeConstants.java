package com.xueyi.common.core.constant;

/**
 * 组织通用常量
 *
 * @author xueyi
 */
public class OrganizeConstants {

    /** 平台内系统用户的唯一标志 */
    public static final String SYS_USER = "SYS_USER";

    /** 更新状态指令 */
    public static final String STATUS_UPDATE_OPERATION = "1";

    /** 企业账号长度限制 */
    public static final int ENTERPRISE_NAME_MIN_LENGTH = 2;

    public static final int ENTERPRISE_NAME_MAX_LENGTH = 30;

    /** 用户名长度限制 */
    public static final int USERNAME_MIN_LENGTH = 2;

    public static final int USERNAME_MAX_LENGTH = 20;

    /** 密码长度限制 */
    public static final int PASSWORD_MIN_LENGTH = 5;

    public static final int PASSWORD_MAX_LENGTH = 20;

    /** 组织树类型 */
    public enum OrganizeType {

        ENTERPRISE("0", "企业级"), DEPT("1", "部门级"), POST("2", "岗位级"), USER("3", "用户级");

        private final String code;
        private final String info;

        OrganizeType(String code, String info) {
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