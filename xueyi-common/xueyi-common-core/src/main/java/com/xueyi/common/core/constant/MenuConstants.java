package com.xueyi.common.core.constant;

/**
 * 菜单常量信息
 *
 * @author ruoyi
 */
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
    public static final Long SYSTEM_TOP_NODE = -1L;

    /** 菜单树 - 顶级节点Id */
    public static final Long MENU_TOP_NODE = 0L;

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 封禁状态 */
    public static final String DISABLE = "1";

    /** 菜单状态（显示） */
    public static final String VISIBLE_TRUE = "Y";

    /** 菜单状态（隐藏） */
    public static final String VISIBLE_FALSE = "N";

    /** 是否缓存（是） */
    public static final String YES_CACHE = "Y";

    /** 是否缓存（否） */
    public static final String NO_CACHE = "N";

    /** 是否菜单外链（是） */
    public static final String YES_FRAME = "Y";

    /** 是否菜单外链（否） */
    public static final String NO_FRAME = "N";

    /** 菜单类型（目录） */
    public static final String TYPE_DIR = "M";

    /** 菜单类型（菜单） */
    public static final String TYPE_MENU = "C";

    /** 菜单类型（按钮） */
    public static final String TYPE_BUTTON = "F";

    /** Layout组件标识 */
    public final static String LAYOUT = "Layout";

    /** ParentView组件标识 */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink组件标识 */
    public final static String INNER_LINK = "InnerLink";
}