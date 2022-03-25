import request from '@/utils/request'

/** 查询字典类型列表 */
export function listDictTypeApi(query) {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params: query
  })
}

/** 查询字典类型选择框列表 */
export function optionDictTypeApi() {
  return request({
    url: '/system/dict/type/option',
    method: 'get'
  })
}

/** 查询字典类型详细 */
export function getDictTypeApi(id) {
  return request({
    url: '/system/dict/type/' + id,
    method: 'get'
  })
}

/** 新增字典类型 */
export function addDictTypeApi(data) {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data: data
  })
}

/** 修改字典类型 */
export function editDictTypeApi(data) {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data: data
  })
}

/** 修改字典类型状态 */
export function editStatusDictTypeApi(id, status) {
  return request({
    url: '/system/dict/type/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除字典类型 */
export function delDictTypeApi(ids) {
  return request({
    url: '/system/dict/type/batch/' + ids.toString(),
    method: 'delete'
  })
}

/** 刷新字典缓存 */
export function refreshDictApi() {
  return request({
    url: '/system/dict/type/refresh',
    method: 'get'
  })
}

