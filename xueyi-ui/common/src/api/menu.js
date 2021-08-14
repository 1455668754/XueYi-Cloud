import request from '../utils/request'
import { acquisition } from '../utils/xueyi'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/system/menu/getRouters',
    method: 'get',
    params: acquisition(null, 'systemId')
  })
}
