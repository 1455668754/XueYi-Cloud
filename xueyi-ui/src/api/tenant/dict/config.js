import request from '@/utils/request'

const Api = {
  LIST_CONFIG: '/system/config/list',
  OPTION_CONFIG: '/system/config/option',
  GET_CONFIG: '/system/config/',
  ADD_CONFIG: '/system/config',
  EDIT_CONFIG: '/system/config',
  DEL_BATCH_CONFIG: '/system/config/batch/',
  DEL_BATCH_FORCE_CONFIG: '/system/config/batch/force/',
  REFRESH_CONFIG: '/system/config/refresh'
}

/** 查询参数列表 */
export function listConfigApi(query) {
  return request({
    url: Api.LIST_CONFIG,
    method: 'get',
    params: query
  })
}

/** 查询参数选择框列表 */
export function optionConfigApi() {
  return request({
    url: Api.OPTION_CONFIG,
    method: 'get'
  })
}

/** 查询参数详细 */
export function getConfigApi(id) {
  return request({
    url: Api.GET_CONFIG + id,
    method: 'get'
  })
}

/** 新增参数 */
export function addConfigApi(data) {
  return request({
    url: Api.ADD_CONFIG,
    method: 'post',
    data: data
  })
}

/** 修改参数 */
export function editConfigApi(data) {
  return request({
    url: Api.EDIT_CONFIG,
    method: 'put',
    data: data
  })
}

/** 删除参数 */
export function delConfigApi(ids) {
  return request({
    url: Api.DEL_BATCH_CONFIG + ids.toString(),
    method: 'delete'
  })
}

/** 强制删除参数 */
export function delForceConfigApi(ids) {
  return request({
    url: Api.DEL_BATCH_FORCE_CONFIG + ids.toString(),
    method: 'delete'
  })
}

/** 刷新参数缓存 */
export function refreshConfigApi() {
  return request({
    url: Api.REFRESH_CONFIG,
    method: 'get'
  })
}
