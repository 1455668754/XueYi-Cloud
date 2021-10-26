package com.xueyi.common.core.constant;

/**
 * 通用常量信息
 * 
 * @author xueyi
 */
public class Constants {

    /** UTF-8 字符集 */
    public static final String UTF8 = "UTF-8";

    /** GBK 字符集 */
    public static final String GBK = "GBK";

    /** RMI 远程方法调用 */
    public static final String LOOKUP_RMI = "rmi://";

    /** LDAP 远程方法调用 */
    public static final String LOOKUP_LDAP = "ldap://";

    /** http请求 */
    public static final String HTTP = "http://";

    /** https请求 */
    public static final String HTTPS = "https://";

    /** ws请求 */
    public static final String WS = "ws://";

    /** wss请求 */
    public static final String WSS = "wss://";

    /** ws请求匹配头 */
    public static final String DEFAULT_FILTER_PATH = "/ws/";

    /** ws请求端口偏移 */
    public static final int WS_PORT = 10;

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

    /** 系统默认值 是 */
    public static final String SYSTEM_DEFAULT_TRUE = "Y";

    /** 系统默认值 否 */
    public static final String SYSTEM_DEFAULT_FALSE = "N";

    /** 正常状态 */
    public static final String STATUS_NORMAL = "0";

    /** 封禁状态 */
    public static final String STATUS_DISABLE = "1";
}
