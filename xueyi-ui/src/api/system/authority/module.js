import request from '@/utils/request'

/** 查询模块列表 */
export function listModuleApi(query) {
  return request({
    url: '/system/module/list',
    method: 'get',
    params: query
  })
}

/** 查询模块选择框列表 */
export function optionModuleApi() {
  return request({
    url: '/system/module/option',
    method: 'get'
  })
}

/** 查询模块详细 */
export function getModuleApi(id) {
  return request({
    url: '/system/module/' + id,
    method: 'get'
  })
}

/** 新增模块 */
export function addModuleApi(data) {
  return request({
    url: '/system/module',
    method: 'post',
    data: data
  })
}

/** 修改模块 */
export function editModuleApi(data) {
  return request({
    url: '/system/module',
    method: 'put',
    data: data
  })
}

/** 修改模块状态 */
export function editStatusModuleApi(id, status) {
  return request({
    url: '/system/module/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除模块 */
export function delModuleApi(ids) {
  return request({
    url: '/system/module/batch/' + ids.toString(),
    method: 'delete'
  })
}
