import request from '@utils/request'

// 查询菜单详细
export function getMenu(data) {
  return request({
    url: '/system/menu/byId',
    method: 'get',
    params: data
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(data) {
  return request({
    url: '/system/menu',
    method: 'delete',
    data: data
  })
}
