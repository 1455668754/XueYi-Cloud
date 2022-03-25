import request from '@/utils/request'

/** 查询调度日志列表 */
export function listJobLogApi(query) {
  return request({
    url: '/schedule/job/log/list',
    method: 'get',
    params: query
  })
}

/** 查询调度日志详细 */
export function getJobLogApi(id) {
  return request({
    url: '/schedule/job/log/' + id,
    method: 'get'
  })
}

/** 删除调度日志 */
export function cleanJobLogApi() {
  return request({
    url: '/schedule/job/log/clean',
    method: 'delete'
  })
}
