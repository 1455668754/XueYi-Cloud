package com.xueyi.common.core.constant;

/**
 * 用户常量信息
 *
 * @author xueyi
 */
public class UserConstants {

    /** 平台内系统用户的唯一标志 */
    public static final String SYS_USER = "SYS_USER";

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 异常状态 */
    public static final String EXCEPTION = "1";

    /** 更新状态指令 */
    public static final String STATUS_UPDATE_OPERATION = "1";

    /** 用户正常状态 */
    public static final String USER_NORMAL = "0";

    /** 用户封禁状态 */
    public static final String USER_DISABLE = "1";

    /** 角色封禁状态 */
    public static final String ROLE_DISABLE = "1";

    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";

    /** 部门停用状态 */
    public static final String DEPT_DISABLE = "1";

    /** 岗位正常状态 */
    public static final String POST_NORMAL = "0";

    /** 岗位停用状态 */
    public static final String POST_DISABLE = "1";

    /** 字典正常状态 */
    public static final String DICT_NORMAL = "0";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";

    public final static String NOT_UNIQUE = "1";

    /** 企业账号长度限制 */
    public static final int ENTERPRISE_NAME_MIN_LENGTH = 2;

    public static final int ENTERPRISE_NAME_MAX_LENGTH = 30;

    /** 用户名长度限制 */
    public static final int USERNAME_MIN_LENGTH = 2;

    public static final int USERNAME_MAX_LENGTH = 20;

    /** 密码长度限制 */
    public static final int PASSWORD_MIN_LENGTH = 5;

    public static final int PASSWORD_MAX_LENGTH = 20;
}