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

  /** 数据范围（1全部数据权限 2自定义数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限 6仅本人数据权限） */
  DATA_SCOPE: {
    ALL: '1',
    CUSTOM: '2',
    DEPT: '3',
    DEPT_AND_CHILD: '4',
    POST: '5',
    SELF: '6'
  },

  /** 默认菜单父Id */
  DEFAULT_PARENT_MENU_ID: '0',

  /** 默认模块 */
  COMMON_MODULE: '1',

  /** 模块缓存 */
  MODULE_CACHE: 'sys.moduleId'
}
