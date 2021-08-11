import request from '@/utils/request'

// 查询子系统模块列表
export function listSystem(query) {
  return request({
    url: '/system/system/list',
    method: 'get',
    params: query
  })
}

// 查询子系统模块详细
export function getSystem(query) {
  return request({
    url: '/system/system/byId',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构
export function treeSelect(query) {
  return request({
    url: '/system/system/roleSystemMenuTreeSelect',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 获取所有权限内模块&菜单 | 无衍生角色
export function treeSelectPermitAll(query) {
  return request({
    url: '/system/system/systemMenuTreePermitAll',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 仅获取超管权限内模块&菜单 | 衍生角色仅获取超管衍生
export function treeSelectPermitAdministrator(query) {
  return request({
    url: '/system/system/systemMenuTreePermitAdministrator',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 仅获取租户权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
export function treeSelectPermitEnterprise(query) {
  return request({
    url: '/system/system/systemMenuTreePermitEnterprise',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
export function treeSelectPermitPersonalScreenDerice(query) {
  return request({
    url: '/system/system/systemMenuTreePermitPersonalScreenDerice',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 仅获取个人权限内模块&菜单 | 衍生角色获取自身组织衍生&超管衍生&租户衍生
export function treeSelectPermitPersonal(query) {
  return request({
    url: '/system/system/systemMenuTreePermitPersonal',
    method: 'get',
    params: query
  })
}

// 新增子系统模块
export function addSystem(data) {
  return request({
    url: '/system/system',
    method: 'post',
    data: data
  })
}

// 修改子系统模块
export function updateSystem(data) {
  return request({
    url: '/system/system',
    method: 'put',
    data: data
  })
}

// 子系统模块状态修改
export function changeSystemStatus(data) {
  return request({
    url: '/system/system/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除子系统模块
export function delSystem(data) {
  return request({
    url: '/system/system/',
    method: 'delete',
    data: data
  })
}
