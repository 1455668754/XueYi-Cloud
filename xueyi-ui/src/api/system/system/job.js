import request from '@/utils/request'

/** 查询调度任务列表 */
export function listJobApi(query) {
  return request({
    url: '/schedule/job/list',
    method: 'get',
    params: query
  })
}

/** 查询调度任务选择框列表 */
export function optionJobApi() {
  return request({
    url: '/schedule/job/option',
    method: 'get'
  })
}

/** 查询调度任务详细 */
export function getJobApi(id) {
  return request({
    url: '/schedule/job/' + id,
    method: 'get'
  })
}

/** 新增调度任务 */
export function addJobApi(data) {
  return request({
    url: '/schedule/job',
    method: 'post',
    data: data
  })
}

/** 修改调度任务 */
export function editJobApi(data) {
  return request({
    url: '/schedule/job',
    method: 'put',
    data: data
  })
}

/** 执行一次调度任务 */
export function runJobApi(id) {
  return request({
    url: '/schedule/job/run/' + id,
    method: 'get'
  })
}

/** 修改调度任务状态 */
export function editStatusJobApi(id, status) {
  return request({
    url: '/schedule/job/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除调度任务 */
export function delJobApi(ids) {
  return request({
    url: '/schedule/job/batch/' + ids.toString(),
    method: 'delete'
  })
}
