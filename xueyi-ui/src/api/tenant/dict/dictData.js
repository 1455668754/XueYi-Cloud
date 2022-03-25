import request from '@/utils/request'

/** 查询字典数据列表 */
export function listDictDataApi(query) {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}

/** 查询字典数据详细 */
export function getDictDataApi(id) {
  return request({
    url: '/system/dict/data/' + id,
    method: 'get'
  })
}

/** 新增字典数据 */
export function addDictDataApi(data) {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data: data
  })
}

/** 修改字典数据 */
export function editDictDataApi(data) {
  return request({
    url: '/system/dict/data',
    method: 'put',
    data: data
  })
}

/** 修改字典数据状态 */
export function editStatusDictDataApi(id, status) {
  return request({
    url: '/system/dict/data/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除字典数据 */
export function delDictDataApi(ids) {
  return request({
    url: '/system/dict/data/batch/' + ids.toString(),
    method: 'delete'
  })
}
