import request from '@/utils/request'

const Api = {
  LIST_STRATEGY: '/tenant/strategy/list',
  OPTION_STRATEGY: '/tenant/strategy/option',
  GET_STRATEGY: '/tenant/strategy/',
  ADD_STRATEGY: '/tenant/strategy',
  EDIT_STRATEGY: '/tenant/strategy',
  EDIT_STATUS_STRATEGY: '/tenant/strategy/status',
  DEL_BATCH_STRATEGY: '/tenant/strategy/batch/'
}

/** 查询数据源策略列表 */
export function listStrategyApi(query) {
  return request({
    url: Api.LIST_STRATEGY,
    method: 'get',
    params: query
  })
}

/** 查询源策略选择框列表 */
export function optionStrategyApi(query) {
  return request({
    url: Api.OPTION_STRATEGY,
    method: 'get'
  })
}

/** 查询数据源策略详细 */
export function getStrategyApi(id) {
  return request({
    url: Api.GET_STRATEGY + id,
    method: 'get'
  })
}

/** 新增数据源策略 */
export function addStrategyApi(data) {
  return request({
    url: Api.ADD_STRATEGY,
    method: 'post',
    data: data
  })
}

/** 修改数据源策略 */
export function editStrategyApi(data) {
  return request({
    url: Api.EDIT_STRATEGY,
    method: 'put',
    data: data
  })
}

/** 修改数据源策略状态 */
export function editStatusStrategyApi(id, status) {
  return request({
    url: Api.EDIT_STATUS_STRATEGY,
    method: 'get',
    data: { id: id, status: status }
  })
}

/** 删除数据源策略 */
export function delStrategyApi(ids) {
  return request({
    url: Api.DEL_BATCH_STRATEGY + ids.toString(),
    method: 'delete'
  })
}
