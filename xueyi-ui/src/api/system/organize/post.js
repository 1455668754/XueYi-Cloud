import request from '@/utils/request'

/** 查询岗位列表 */
export function listPostApi(query) {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

/** 查询岗位选择框列表 */
export function optionPostApi() {
  return request({
    url: '/system/post/option',
    method: 'get'
  })
}

/** 查询岗位详细 */
export function getPostApi(id) {
  return request({
    url: '/system/post/' + id,
    method: 'get'
  })
}

/** 查询岗位的角色权限节点集 */
export function getAuthPostApi(id) {
  return request({
    url: '/system/post/auth/' + id,
    method: 'get'
  })
}

/** 新增岗位 */
export function addPostApi(data) {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

/** 修改岗位 */
export function editPostApi(data) {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

/** 修改岗位的角色权限 */
export function editAuthPostScopeApi(id, roleIds) {
  return request({
    url: '/system/post/auth',
    method: 'put',
    data: { id: id, roleIds: roleIds }
  })
}

/** 修改岗位状态 */
export function editStatusPostApi(id, status) {
  return request({
    url: '/system/post/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除岗位 */
export function delPostApi(ids) {
  return request({
    url: '/system/post/batch/' + ids.toString(),
    method: 'delete'
  })
}
