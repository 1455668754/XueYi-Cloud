import request from '@/utils/request'

/** 查询用户列表 */
export function listUserApi(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

/** 查询用户选择框列表 */
export function optionUserApi() {
  return request({
    url: '/system/user/option',
    method: 'get'
  })
}

/** 查询用户详细 */
export function getUserApi(id) {
  return request({
    url: '/system/user/' + id,
    method: 'get'
  })
}

/** 查询用户的角色权限节点集 */
export function getAuthUserApi(id) {
  return request({
    url: '/system/user/auth/' + id,
    method: 'get'
  })
}

/** 新增用户 */
export function addUserApi(data) {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}

/** 修改用户 */
export function editUserApi(data) {
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}

/** 修改用户的角色权限 */
export function editAuthUserScopeApi(id, roleIds) {
  return request({
    url: '/system/user/auth',
    method: 'put',
    data: { id: id, roleIds: roleIds }
  })
}

/** 修改用户状态 */
export function editStatusUserApi(id, status) {
  return request({
    url: '/system/user/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除用户 */
export function delUserApi(ids) {
  return request({
    url: '/system/user/batch/' + ids.toString(),
    method: 'delete'
  })
}

/** 用户密码重置 */
export function resetUserPwdApi(id, password) {
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: { id: id, password: password }
  })
}
