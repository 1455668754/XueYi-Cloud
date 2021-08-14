import request from '@utils/request'

// 查询登录日志列表
export function list(query) {
  return request({
    url: '/system/loginInfo/list',
    method: 'get',
    params: query
  })
}

// 删除登录日志
export function delLoginInfo(data) {
  return request({
    url: '/system/loginInfo',
    method: 'delete',
    data: data
  })
}

// 清空登录日志
export function cleanLoginInfo() {
  return request({
    url: '/system/loginInfo/clean',
    method: 'delete'
  })
}
