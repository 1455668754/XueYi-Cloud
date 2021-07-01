import request from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(query) {
  return request({
    url: '/system/dept/list/exclude',
    method: 'get',
    params: query
  })
}

// 查询部门详细
export function getDept(query) {
  return request({
    url: '/system/dept/byId',
    method: 'get',
    params: query
  })
}

// 查询部门下拉树结构
export function treeSelect() {
  return request({
    url: '/system/dept/treeSelect',
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/system/dept',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/system/dept',
    method: 'put',
    data: data
  })
}

// 修改部门-角色关系
export function changeDeptRole(data) {
  return request({
    url: '/system/dept/changeDeptRole',
    method: 'put',
    data: data
  })
}

// 部门状态修改
export function changeDeptStatus(data) {
  return request({
    url: '/system/dept/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(data) {
  return request({
    url: '/system/dept',
    method: 'delete',
    data: data
  })
}
