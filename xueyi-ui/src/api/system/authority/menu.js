import request from '@/utils/request'

/** 根据菜单类型获取指定模块的可配菜单集 */
export function getMenuRouteListApi(moduleId, menuType) {
  return request({
    url: '/system/menu/routeList',
    method: 'post',
    data: { moduleId: moduleId, menuType: menuType }
  })
}
