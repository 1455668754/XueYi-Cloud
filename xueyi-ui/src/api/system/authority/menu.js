import request from '@/utils/request'

/** 查询菜单列表 */
export function listMenuApi(query) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

/** 查询菜单列表（排除节点） */
export function listMenuExNodesApi(id, moduleId) {
  return request({
    url: '/system/menu/list/exclude',
    method: 'get',
    params: { id: id, moduleId: moduleId }
  })
}

/** 根据菜单类型获取指定模块的可配菜单集 */
export function getMenuRouteListApi(moduleId, menuType) {
  return request({
    url: '/system/menu/routeList',
    method: 'post',
    data: { moduleId: moduleId, menuType: menuType }
  })
}

/** 根据菜单类型获取指定模块的可配菜单集（不包含自己及其子集） */
export function getMenuRouteListExNodesApi(id, moduleId, menuType) {
  return request({
    url: '/system/menu/routeList/exclude',
    method: 'post',
    data: { id: id, moduleId: moduleId, menuType: menuType }
  })
}

/** 查询菜单详细 */
export function getMenuApi(id) {
  return request({
    url: '/system/menu/' + id,
    method: 'get'
  })
}

/** 新增菜单 */
export function addMenuApi(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

/** 修改菜单 */
export function editMenuApi(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

/** 修改菜单状态 */
export function editStatusMenuApi(id, status) {
  return request({
    url: '/system/menu/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除菜单 */
export function delMenuApi(ids) {
  return request({
    url: '/system/menu/batch/' + ids.toString(),
    method: 'delete'
  })
}
