import request from '@/utils/request'

/** 查询数据源策略列表 */
export function listStrategyApi(query) {
  return request({
    url: '/tenant/strategy/list',
    method: 'get',
    params: query
  })
}

/** 查询源策略选择框列表 */
export function optionStrategyApi(query) {
  return request({
    url: '/tenant/strategy/option',
    method: 'get'
  })
}

/** 查询数据源策略详细 */
export function getStrategyApi(id) {
  return request({
    url: '/tenant/strategy/' + id,
    method: 'get'
  })
}

/** 新增数据源策略 */
export function addStrategyApi(data) {
  return request({
    url: '/tenant/strategy',
    method: 'post',
    data: data
  })
}

/** 修改数据源策略 */
export function editStrategyApi(data) {
  return request({
    url: '/tenant/strategy',
    method: 'put',
    data: data
  })
}

/** 修改数据源策略状态 */
export function editStatusStrategyApi(id, status) {
  return request({
    url: '/tenant/strategy/status',
    method: 'get',
    data: { id: id, status: status }
  })
}

/** 删除数据源策略 */
export function delStrategyApi(ids) {
  return request({
    url: '/tenant/strategy/batch/' + ids.toString(),
    method: 'delete'
  })
}
