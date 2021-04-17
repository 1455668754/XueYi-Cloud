import request from '@/utils/request'

// 查询Logo
export function getLogo() {
  return request({
    url: '/system/enterprise/logo',
    method: 'get',
  })
}
