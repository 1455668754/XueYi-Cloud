package com.xueyi.common.core.constant;

/**
 * 权限通用常量
 *
 * @author xueyi
 */
public class AuthorityConstants {

    /** 用户类型 - 普通用户 */
    public static final String USER_TYPE_NORMAL = "01";

    /** 用户类型 - 超管用户 */
    public static final String USER_TYPE_ADMIN = "00";

    /** 租户类型 - 普通租户 */
    public static final String TENANT_TYPE_NORMAL = "N";

    /** 租户类型 - 超管租户 */
    public static final String TENANT_TYPE_ADMIN = "Y";

    /** 公共数据 - Y是 */
    public static final String IS_COMMON_TRUE = "Y";

    /** 公共数据 - N否 */
    public static final String IS_COMMON_FALSE = "N";

    /** 公共企业Id */
    public static final Long COMMON_ENTERPRISE = 0L;

    /** 公共企业Id */
    public static final Long COMMON_USER = 0L;

    /** 角色类型 - 常规 */
    public static final String NORMAL_TYPE = "0";

    /** 角色类型 - 租户衍生 */
    public static final String DERIVE_TENANT_TYPE = "1";

    /** 角色类型 - 企业衍生 */
    public static final String DERIVE_ENTERPRISE_TYPE = "2";

    /** 角色类型 - 部门衍生 */
    public static final String DERIVE_DEPT_TYPE = "3";

    /** 角色类型 - 岗位衍生 */
    public static final String DERIVE_POST_TYPE = "4";

    /** 角色类型 - 用户衍生 */
    public static final String DERIVE_USER_TYPE = "5";

    /** 数据范围 - 全部数据权限 */
    public static final String DATA_SCOPE_ALL = "1";

    /** 数据范围 - 自定义数据权限 */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /** 数据范围 - 本部门数据权限 */
    public static final String DATA_SCOPE_DEPT = "3";

    /** 数据范围 - 本部门及以下数据权限 */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /** 数据范围 - 本岗位数据权限 */
    public static final String DATA_SCOPE_POST = "5";

    /** 数据范围 - 仅本人数据权限 */
    public static final String DATA_SCOPE_SELF = "6";
}