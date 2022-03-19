import request from '@/utils/request'

// 获取菜单路由
export const getMenuRouters = (moduleId) => {
  return request({
    url: '/system/menu/getRouters/' + moduleId,
    method: 'get',
  })
}

// 获取模块路由
export const getModuleRouters = () => {
  return request({
    url: '/system/module/getRouters',
    method: 'get'
  })
}
