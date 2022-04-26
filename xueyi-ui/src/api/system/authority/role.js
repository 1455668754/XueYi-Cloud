import request from '@/utils/request'

/** 查询角色列表 */
export function listRoleApi(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

/** 查询角色选择框列表 */
export function optionRoleApi() {
  return request({
    url: '/system/role/option',
    method: 'get'
  })
}

/** 查询角色详细 */
export function getRoleApi(id) {
  return request({
    url: '/system/role/' + id,
    method: 'get'
  })
}

/** 查询角色功能权限节点集（叶子节点） */
export function getAuthRoleApi(id) {
  return request({
    url: '/system/role/auth/' + id,
    method: 'get'
  })
}

/** 查询角色数据权限 */
export function getOrganizeRoleApi(id) {
  return request({
    url: '/system/role/organize/' + id,
    method: 'get'
  })
}

/** 新增角色 */
export function addRoleApi(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

/** 修改角色 */
export function editRoleApi(data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

/** 修改角色功能权限 */
export function editAuthScopeApi(id, authIds) {
  return request({
    url: '/system/role/auth',
    method: 'put',
    data: { id: id, authIds: authIds }
  })
}

/** 修改角色数据权限 */
export function editDataScopeApi(data) {
  return request({
    url: '/system/role/organize',
    method: 'put',
    data: data
  })
}

/** 修改角色状态 */
export function editStatusRoleApi(id, status) {
  return request({
    url: '/system/role/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除角色 */
export function delRoleApi(ids) {
  return request({
    url: '/system/role/batch/' + ids.toString(),
    method: 'delete'
  })
}
