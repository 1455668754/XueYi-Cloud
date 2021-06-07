import request from '@/utils/request'
import { addSystem } from '@/utils/xueyi'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/system/menu/getRouters',
    method: 'get',
    params: addSystem()
  })
}
