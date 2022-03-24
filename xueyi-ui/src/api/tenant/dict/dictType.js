import request from '@/utils/request'

const Api = {
  LIST_DICT_TYPE: '/system/dict/type/list',
  OPTION_DICT_TYPE: '/system/dict/type/option',
  GET_DICT_TYPE: '/system/dict/type/',
  ADD_DICT_TYPE: '/system/dict/type',
  EDIT_DICT_TYPE: '/system/dict/type',
  EDIT_STATUS_DICT_TYPE: '/system/dict/type/status',
  DEL_BATCH_DICT_TYPE: '/system/dict/type/batch/',
  REFRESH_DICT: '/system/dict/type/refresh'
}

/** 查询字典类型列表 */
export function listDictTypeApi(query) {
  return request({
    url: Api.LIST_DICT_TYPE,
    method: 'get',
    params: query
  })
}

/** 查询字典类型选择框列表 */
export function optionDictTypeApi() {
  return request({
    url: Api.OPTION_DICT_TYPE,
    method: 'get'
  })
}

/** 查询字典类型详细 */
export function getDictTypeApi(id) {
  return request({
    url: Api.GET_DICT_TYPE + id,
    method: 'get'
  })
}

/** 新增字典类型 */
export function addDictTypeApi(data) {
  return request({
    url: Api.ADD_DICT_TYPE,
    method: 'post',
    data: data
  })
}

/** 修改字典类型 */
export function editDictTypeApi(data) {
  return request({
    url: Api.EDIT_DICT_TYPE,
    method: 'put',
    data: data
  })
}

/** 修改字典类型状态 */
export function editStatusDictTypeApi(id, status) {
  return request({
    url: Api.EDIT_STATUS_DICT_TYPE,
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除字典类型 */
export function delDictTypeApi(ids) {
  return request({
    url: Api.DEL_BATCH_DICT_TYPE + ids.toString(),
    method: 'delete'
  })
}

/** 刷新字典缓存 */
export const refreshDictApi = () =>
  defaultRequest.get({ url: Api.REFRESH_DICT })

export function listConfigApi(query) {
  return request({
    url: Api.LIST_CONFIG,
    method: 'get',
    params: query
  })
}
