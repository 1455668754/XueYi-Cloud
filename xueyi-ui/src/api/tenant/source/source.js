import request from '@/utils/request'

/** 查询数据源列表 */
export function listSourceApi(query) {
  return request({
    url: '/tenant/source/list',
    method: 'get',
    params: query
  })
}

/** 查询数据源选择框列表 */
export function optionSourceApi() {
  return request({
    url: '/tenant/source/option',
    method: 'get'
  })
}

/** 查询数据源详细 */
export function getSourceApi(id) {
  return request({
    url: '/tenant/source/' + id,
    method: 'get'
  })
}

/** 数据源连接测试 */
export function connectionSourceApi(data) {
  return request({
    url: '/tenant/source/connection',
    method: 'post',
    data: data
  })
}

/** 新增数据源 */
export function addSourceApi(data) {
  return request({
    url: '/tenant/source',
    method: 'post',
    data: data
  })
}

/** 修改数据源 */
export function editSourceApi(data) {
  return request({
    url: '/tenant/source',
    method: 'put',
    data: data
  })
}

/** 修改数据源状态 */
export function editStatusSourceApi(id, status) {
  return request({
    url: '/tenant/source/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除数据源 */
export function delSourceApi(ids) {
  return request({
    url: '/tenant/source/batch/' + ids.toString(),
    method: 'delete'
  })
}
