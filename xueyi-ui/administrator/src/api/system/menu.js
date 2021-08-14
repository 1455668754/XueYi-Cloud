import request from '@utils/request'
import { addSystem } from '@utils/xueyi'

// 查询菜单列表
export function listMenu(data) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: addSystem(data)
  })
}

// 查询菜单详细
export function getMenu(data) {
  return request({
    url: '/system/menu/byId',
    method: 'get',
    params: addSystem(data)
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: addSystem(data)
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: addSystem(data)
  })
}

// 删除菜单
export function delMenu(data) {
  return request({
    url: '/system/menu',
    method: 'delete',
    data: addSystem(data)
  })
}
