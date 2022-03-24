/** 租户权限标识 */
export const TenantAuth = {
  // 查看租户列表
  LIST: 'tenant:tenant:list',
  // 查询租户详情
  SINGLE: 'tenant:tenant:single',
  // 新增租户
  ADD: 'tenant:tenant:add',
  // 修改租户
  EDIT: 'tenant:tenant:edit',
  // 修改租户状态
  EDIT_STATUS: 'tenant:tenant:es',
  // 租户权限分配
  AUTH: 'tenant:tenant:auth',
  // 删除租户
  DELETE: 'tenant:tenant:delete',
  // 租户导入
  IMPORT: 'tenant:tenant:import',
  // 租户导出
  EXPORT: 'tenant:tenant:export'
}
