import request from '@/utils/request'

const Api = {
  LIST_DICT_DATA: '/system/dict/data/list',
  GET_DICT_DATA: '/system/dict/data/',
  ADD_DICT_DATA: '/system/dict/data',
  EDIT_DICT_DATA: '/system/dict/data',
  EDIT_STATUS_DICT_DATA: '/system/dict/data/status',
  DEL_BATCH_DICT_DATA: '/system/dict/data/batch/'
}

/** 查询字典数据列表 */
export function listDictDataApi(query) {
  return request({
    url: Api.LIST_DICT_DATA,
    method: 'get',
    params: query
  })
}

/** 查询字典数据详细 */
export function getDictDataApi(id) {
  return request({
    url: Api.GET_DICT_DATA + id,
    method: 'get'
  })
}

/** 新增字典数据 */
export function addDictDataApi(data) {
  return request({
    url: Api.ADD_DICT_DATA,
    method: 'post',
    data: data
  })
}

/** 修改字典数据 */
export function editDictDataApi(data) {
  return request({
    url: Api.EDIT_DICT_DATA,
    method: 'put',
    data: data
  })
}

/** 修改字典数据状态 */
export function editStatusDictDataApi(id, status) {
  return request({
    url: Api.EDIT_STATUS_DICT_DATA,
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除字典数据 */
export function delDictDataApi(ids) {
  return request({
    url: Api.DEL_BATCH_DICT_DATA + ids.toString(),
    method: 'delete'
  })
}
