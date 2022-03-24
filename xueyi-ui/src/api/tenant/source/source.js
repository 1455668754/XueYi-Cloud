import request from '@/utils/request'

const Api = {
  LIST_SOURCE: '/tenant/source/list',
  OPTION_SOURCE: '/tenant/source/option',
  GET_SOURCE: '/tenant/source/',
  CONNECTION: '/tenant/source/connection',
  ADD_SOURCE: '/tenant/source',
  EDIT_SOURCE: '/tenant/source',
  EDIT_STATUS_SOURCE: '/tenant/source/status',
  DEL_BATCH_SOURCE: '/tenant/source/batch/'
}

/** 查询数据源列表 */
export function listSourceApi(query) {
  return request({
    url: Api.LIST_SOURCE,
    method: 'get',
    params: query
  })
}

/** 查询数据源选择框列表 */
export function optionSourceApi() {
  return request({
    url: Api.OPTION_SOURCE,
    method: 'get'
  })
}

/** 查询数据源详细 */
export function getSourceApi(id) {
  return request({
    url: Api.GET_SOURCE + id,
    method: 'get'
  })
}

/** 数据源连接测试 */
export function connectionSourceApi(data) {
  return request({
    url: Api.CONNECTION,
    method: 'post',
    data: data
  })
}

/** 新增数据源 */
export function addSourceApi(data) {
  return request({
    url: Api.ADD_SOURCE,
    method: 'post',
    data: data
  })
}

/** 修改数据源 */
export function editSourceApi(data) {
  return request({
    url: Api.EDIT_SOURCE,
    method: 'put',
    data: data
  })
}

/** 修改数据源状态 */
export function editStatusSourceApi(id, status) {
  return request({
    url: Api.EDIT_STATUS_SOURCE,
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除数据源 */
export function delSourceApi(ids) {
  return request({
    url: Api.DEL_BATCH_SOURCE + ids.toString(),
    method: 'delete'
  })
}
