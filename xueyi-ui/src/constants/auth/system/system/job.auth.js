/** 调度任务权限标识 */
export const JobAuth = {
  // 查看调度任务列表
  LIST: 'schedule:job:list',
  // 查询调度任务详情
  SINGLE: 'schedule:job:single',
  // 新增调度任务
  ADD: 'schedule:job:add',
  // 修改调度任务
  EDIT: 'schedule:job:edit',
  // 修改调度任务状态
  EDIT_STATUS: 'schedule:job:es',
  // 删除调度任务
  DELETE: 'schedule:job:delete',
  // 调度任务导入
  IMPORT: 'schedule:job:import',
  // 调度任务导出
  EXPORT: 'schedule:job:export'
}
