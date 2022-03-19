package com.xueyi.common.core.constant.basic;

/**
 * 通用常量
 * 
 * @author xueyi
 */
public class Constants {

    /** 成功标记 */
    public static final Integer SUCCESS = 200;

    /** 失败标记 */
    public static final Integer FAIL = 500;

    /** 登录成功 */
    public static final String LOGIN_SUCCESS = "Success";

    /** 注销 */
    public static final String LOGOUT = "Logout";

    /** 注册 */
    public static final String REGISTER = "Register";

    /** 登录失败 */
    public static final String LOGIN_FAIL = "Error";

    /** 当前记录起始索引 */
    public static final String PAGE_NUM = "pageNum";

    /** 每页显示记录数 */
    public static final String PAGE_SIZE = "pageSize";

    /** 排序列 */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /** 排序的方向 "desc" 或者 "asc" */
    public static final String IS_ASC = "isAsc";

    /** 验证码有效期（分钟） */
    public static final long CAPTCHA_EXPIRATION = 2;

    /** 资源映射路径 前缀 */
    public static final String RESOURCE_PREFIX = "/profile";

}
