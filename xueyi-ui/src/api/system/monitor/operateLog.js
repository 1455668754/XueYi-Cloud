import request from '@/utils/request'

/** 查询操作日志列表 */
export function listOperateLogApi(query) {
  return request({
    url: '/system/operateLog/list',
    method: 'get',
    params: query
  })
}

/** 查询操作日志详细 */
export function getOperateLogApi(id) {
  return request({
    url: '/system/operateLog/' + id,
    method: 'get'
  })
}

/** 删除操作日志 */
export function delOperateLogApi(ids) {
  return request({
    url: '/system/operateLog/batch/' + ids.toString(),
    method: 'delete'
  })
}

/** 清空操作日志 */
export function cleanOperateLogApi() {
  return request({
    url: '/system/operateLog/clean',
    method: 'delete'
  })
}
