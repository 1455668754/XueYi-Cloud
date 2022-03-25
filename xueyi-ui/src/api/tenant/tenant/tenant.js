import request from '@/utils/request'

/** 查询租户列表 */
export function listTenantApi(query) {
  return request({
    url: '/tenant/tenant/list',
    method: 'get',
    params: query
  })
}

/** 查询租户详细 */
export function getTenantApi(id) {
  return request({
    url: '/tenant/tenant/' + id,
    method: 'get'
  })
}

/** 查询租户权限（叶子节点） */
export function getAuthTenantApi(id) {
  return request({
    url: '/tenant/tenant/auth/' + id,
    method: 'get'
  })
}

/** 查询公共权限范围树 */
export function authScopeTenantApi() {
  return request({
    url: '/system/auth/tenant/authScope',
    method: 'get'
  })
}

/** 新增租户 */
export function addTenantApi(data) {
  return request({
    url: '/tenant/tenant',
    method: 'post',
    data: data
  })
}

/** 修改租户 */
export function editTenantApi(data) {
  return request({
    url: '/tenant/tenant',
    method: 'put',
    data: data
  })
}

/** 修改租户权限 */
export function editAuthTenantApi(data) {
  return request({
    url: '/tenant/tenant/auth',
    method: 'put',
    data: data
  })
}

/** 修改租户状态 */
export function editStatusTenantApi(id, status) {
  return request({
    url: '/tenant/tenant/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除租户 */
export function delTenantApi(ids) {
  return request({
    url: '/tenant/tenant/batch/' + ids.toString(),
    method: 'delete'
  })
}
