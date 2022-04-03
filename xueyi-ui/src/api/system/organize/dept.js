import request from '@/utils/request'

/** 查询部门列表 */
export function listDeptApi(query) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params: query
  })
}

/** 查询部门列表（排除节点） */
export function listDeptExNodesApi(id) {
  return request({
    url: '/system/dept/list/exclude',
    method: 'get',
    params: { id: id }
  })
}

/** 查询部门选择框列表 */
export function optionDeptApi(query) {
  return request({
    url: '/system/dept/option',
    method: 'get',
    params: query
  })
}

/** 查询部门详细 */
export function getDeptApi(id) {
  return request({
    url: '/system/dept/' + id,
    method: 'get'
  })
}

/** 查询部门的角色权限节点集 */
export function getAuthDeptApi(id) {
  return request({
    url: '/system/dept/auth/' + id,
    method: 'get'
  })
}

/** 新增部门 */
export function addDeptApi(data) {
  return request({
    url: '/system/dept',
    method: 'post',
    data: data
  })
}

/** 修改部门 */
export function editDeptApi(data) {
  return request({
    url: '/system/dept',
    method: 'put',
    data: data
  })
}

/** 修改部门的角色权限 */
export function editAuthDeptScopeApi(id, roleIds) {
  return request({
    url: '/system/dept/auth',
    method: 'put',
    data: { id: id, roleIds: roleIds }
  })
}

/** 修改部门状态 */
export function editStatusDeptApi(id, status) {
  return request({
    url: '/system/dept/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除部门 */
export function delDeptApi(ids) {
  return request({
    url: '/system/dept/batch/' + ids.toString(),
    method: 'delete'
  })
}
