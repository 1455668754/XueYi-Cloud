import request from '@/utils/request'

// 登录方法
export function login(enterpriseName, username, password, code, uuid) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: { enterpriseName, username, password, code, uuid }
  })
}

// 刷新方法
export function refreshToken() {
  return request({
    url: '/auth/refresh',
    method: 'post'
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/getInfo',
    method: 'get'
  })
}

// 获取企业信息
export function getEnterpriseProfile() {
  return request({
    url: '/system/enterprise/profile',
    method: 'get',
  })
}

// 获取首页可展示子系统模块列表
export function viewListSystem() {
  return request({
    url: '/system/system/viewList',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'delete'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/code',
    method: 'get'
  })
}
