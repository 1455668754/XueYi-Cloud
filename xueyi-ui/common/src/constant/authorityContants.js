module.exports = {

    /** 是否缓存（Y是 N否） */
    CACHE: {
        YES: 'Y',
        NO: 'N'
    },

    /** 是否菜单外链（Y是 N否） */
    FRAME: {
        YES: 'Y',
        NO: 'N'
    },

    /** 菜单类型（M目录 C菜单 F按钮） */
    MENU_TYPE: {
        DIR: 'M',
        MENU: 'C',
        BUTTON: 'F'
    },

    /** 类型（0模块 1菜单） */
    TYPE: {
        SYSTEM: '0',
        MENU: '1'
    },

    /** 组件标识（Layout组件  ParentView组件 InnerLink组件） */
    COMPONENT: {
        LAY_OUT: 'Layout',
        PARENT_VIEW: 'ParentView',
        INNER_LINK: 'InnerLink'
    },

    /** 跳转类型（0内部跳转 1外部跳转） */
    SYSTEM_TYPE: {
        INSIDE: '0',
        EXTERNAL: '1'
    },

    /** 是否打开新页（Y是 N否） */
    IS_NEW: {
        YES: 'Y',
        NO: 'N'
    },

    /** 默认菜单父Id */
    DEFAULT_PARENT_MENU_ID: '0',

    /** 默认系统Id */
    DEFAULT_SYSTEM_ID: '0'
}
