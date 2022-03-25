import request from '@/utils/request'

/** 查询企业权限范围树 */
export function authScopeEnterpriseApi() {
  return request({
    url: '/system/auth/enterprise/authScope',
    method: 'get'
  })
}
