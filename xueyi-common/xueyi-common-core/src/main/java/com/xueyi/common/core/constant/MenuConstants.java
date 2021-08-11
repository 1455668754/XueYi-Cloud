package com.xueyi.common.core.constant;

public class MenuConstants {

    /** 控制参数 - 获取所有权限内模块&菜单 | 无衍生角色 */
    public static final String PERMIT_ALL = "0";

    /** 控制参数 - 获取所有权限内模块&菜单 | 无衍生角色 | 仅公共数据 */
    public static final String PERMIT_ALL_ONLY_PUBLIC = "1";

    /** 控制参数 - 仅获取超管权限内模块&菜单 | 衍生角色仅获取超管衍生 */
    public static final String PERMIT_ADMINISTRATOR = "2";

    /** 控制参数 - 仅获取租户权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生 */
    public static final String PERMIT_ENTERPRISE = "3";

    /** 控制参数 - 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生 */
    public static final String PERMIT_PERSONAL_SCREEN_DERIVE = "4";

    /** 控制参数 - 仅获取个人权限内模块&菜单 | 衍生角色获取自身组织衍生&超管衍生&租户衍生 */
    public static final String PERMIT_PERSONAL = "5";

    /** 模块&菜单树 - 顶级节点Id */
    public static final Long TOP_NODE = -1L;

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 封禁状态 */
    public static final String DISABLE = "1";
}