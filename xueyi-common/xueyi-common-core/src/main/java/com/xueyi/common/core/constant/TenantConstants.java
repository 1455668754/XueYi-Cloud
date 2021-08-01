package com.xueyi.common.core.constant;

/**
 * 租户常量信息
 *
 * @author xueyi
 */
public class TenantConstants {

    /** 注册租户默认策略Id */
    public static final Long REGISTER_TENANT_STRATEGY_ID = 1L;

    /** 注册租户默认策略数据源 */
    public static final String REGISTER_TENANT_STRATEGY_SOURCE = "slave1";

    /** 默认主数据源 */
    public static final String MAIN_SOURCE = "master";

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 异常状态 */
    public static final String EXCEPTION = "1";

    /** 停用状态 */
    public static final String DISABLE = "1";

    /** 子数据源 */
    public static final String SLAVE_SOURCE = "0";

    /** 主数据源 */
    public static final String MASTER_SOURCE = "1";

    /** 数据源读写类型 */
    public static final String SOURCE_READ_WRITE = "0";

    /** 数据源只读类型 */
    public static final String SOURCE_READ = "1";

    /** 数据源只写类型 */
    public static final String SOURCE_WRITE = "2";
}