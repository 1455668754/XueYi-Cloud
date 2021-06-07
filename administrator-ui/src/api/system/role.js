import request from '@/utils/request'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole(roleId) {
  return request({
    url: '/system/role/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

// 角色菜单权限获取
export function getMenuScope(roleId) {
  return request({
    url: '/system/role/menuScope/'+roleId,
    method: 'get'
  })
}

// 角色数据权限获取
export function getDataScope(roleId) {
  return request({
    url: '/system/role/dataScope/'+roleId,
    method: 'get'
  })
}

// 角色菜单权限
export function menuScope(data) {
  return request({
    url: '/system/role/menuScope/',
    method: 'put',
    data: data
  })
}

// 角色数据权限
export function dataScope(data) {
  return request({
    url: '/system/role/dataScope/',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeRoleStatus(roleId, status) {
  const data = {
    roleId,
    status
  }
  return request({
    url: '/system/role/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(roleId) {
  return request({
    url: '/system/role/' + roleId,
    method: 'delete'
  })
}

// 角色下拉选择列表
export function optionSelect() {
  return request({
    url: '/system/role/optionSelect',
    method: 'get'
  })
}
