/** 租户标识（Y租管 N普通） */
export const TenantTypeEnum = {
  // 租管租户
  ADMIN: 'Y',
  // 普通租户
  NORMAL: 'N'
}

/** 字典：状态（0正常 1停用） */
export const DicStatusEnum = {
  // 正常
  NORMAL: '0',
  // 停用
  DISABLE: '1',
  // 异常
  EXCEPTION: '1'
}

/** 字典：序号 */
export const DicSortEnum = {
  ZERO: 0,
  ONE: 1,
  TWO: 2,
  THREE: 3,
  FOUR: 4,
  FIVE: 5
}

/** 字典：是否列表（Y是 N否） */
export const DicYesNoEnum = {
  // 是
  YES: 'Y',
  // 否
  NO: 'N'
}

/** 字典：显隐列表（0显示 1隐藏） */
export const DicShowHideEnum = {
  // 显示
  SHOW: '0',
  // 隐藏
  HIDE: '1'
}

/** 字典：公共私有列表（0公共 1私有） */
export const DicCommonPrivateEnum = {
  // 公共
  COMMON: '0',
  // 私有
  PRIVATE: '1'
}

/** 图标枚举 */
export const IconEnum = {
  VIEW: 'el-icon-view',
  ADD: 'el-icon-plus',
  IMPORT: 'ant-design:vertical-align-top-outlined',
  EXPORT: 'el-icon-download',
  EDIT: 'el-icon-edit',
  AUTH: 'el-icon-circle-check',
  DATA: 'clarity:note-edit-line',
  DELETE: 'el-icon-delete',
  SEARCH: 'el-icon-search',
  RESET: 'el-icon-refresh',
  UPLOAD: 'el-icon-upload',
  DOWNLOAD: 'el-icon-download',
  PREVIEW: 'ant-design:eye-outlined',
  ADD_FOLD: 'ant-design:folder-add-outlined',
  LOG: 'ant-design:exception-outlined',
  EDIT_PASSWORD: 'el-icon-key'
}


/** 默认模块 */
export const COMMON_MODULE = '1'

/** 默认模块 */
export const DEFAULT_TREE_NODE = '0'
