import request from '../utils/request'
import { acquisition } from '../utils/xueyi'

// 获取菜单路由
export const getRouters = () => {
  return request({
    url: '/system/menu/getRouters',
    method: 'get',
    params: acquisition(null, 'systemId')
  })
}

// 获取首页模块路由
export function getSystemRoutes() {
  return request({
    url: '/system/system/getSystemRoutes',
    method: 'get'
  })
}
