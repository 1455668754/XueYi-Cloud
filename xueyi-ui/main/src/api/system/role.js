import request from '@utils/request'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole(query) {
  return request({
    url: '/system/role/byId',
    method: 'get',
    params: query
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

// 修改保存角色数据权限
export function dataScope(data) {
  return request({
    url: '/system/role/dataScope/save',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeRoleStatus(data) {
  return request({
    url: '/system/role/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(data) {
  return request({
    url: '/system/role',
    method: 'delete',
    data: data
  })
}

// 角色下拉选择列表
export function optionSelect() {
  return request({
    url: '/system/role/optionSelect',
    method: 'get'
  })
}
