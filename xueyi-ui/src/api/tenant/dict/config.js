import request from '@/utils/request'

/** 查询参数列表 */
export function listConfigApi(query) {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

/** 查询参数选择框列表 */
export function optionConfigApi() {
  return request({
    url: '/system/config/option',
    method: 'get'
  })
}

/** 查询参数详细 */
export function getConfigApi(id) {
  return request({
    url: '/system/config/' + id,
    method: 'get'
  })
}

/** 新增参数 */
export function addConfigApi(data) {
  return request({
    url: '/system/config',
    method: 'post',
    data: data
  })
}

/** 修改参数 */
export function editConfigApi(data) {
  return request({
    url: '/system/config',
    method: 'put',
    data: data
  })
}

/** 删除参数 */
export function delConfigApi(ids) {
  return request({
    url: '/system/config/batch/' + ids.toString(),
    method: 'delete'
  })
}

/** 强制删除参数 */
export function delForceConfigApi(ids) {
  return request({
    url: '/system/config/batch/force/' + ids.toString(),
    method: 'delete'
  })
}

/** 刷新参数缓存 */
export function refreshConfigApi() {
  return request({
    url: '/system/config/refresh',
    method: 'get'
  })
}
