import request from '@/utils/request'

const Api = {
  LIST_TENANT: '/tenant/tenant/list',
  GET_TENANT: '/tenant/tenant/',
  AUTH_SCOPE_TENANT: '/system/auth/tenant/authScope',
  GET_AUTH_TENANT: '/tenant/tenant/auth/',
  EDIT_AUTH_TENANT: '/tenant/tenant/auth',
  ADD_TENANT: '/tenant/tenant',
  EDIT_TENANT: '/tenant/tenant',
  EDIT_STATUS_TENANT: '/tenant/tenant/status',
  DEL_BATCH_TENANT: '/tenant/tenant/batch/'
}

/** 查询租户列表 */
export function listTenantApi(query) {
  return request({
    url: Api.LIST_TENANT,
    method: 'get',
    params: query
  })
}

/** 查询租户详细 */
export function getTenantApi(id) {
  return request({
    url: Api.GET_TENANT + id,
    method: 'get'
  })
}

/** 查询租户权限（叶子节点） */
export function getAuthTenantApi(id) {
  return request({
    url: Api.GET_AUTH_TENANT + id,
    method: 'get'
  })
}

/** 查询公共权限范围树 */
export function authScopeTenantApi() {
  return request({
    url: Api.AUTH_SCOPE_TENANT,
    method: 'get'
  })
}

/** 新增租户 */
export function addTenantApi(data) {
  return request({
    url: Api.ADD_TENANT,
    method: 'post',
    data: data
  })
}

/** 修改租户 */
export function editTenantApi(data) {
  return request({
    url: Api.EDIT_TENANT,
    method: 'put',
    data: data
  })
}

/** 修改租户权限 */
export function editAuthTenantApi(data) {
  return request({
    url: Api.EDIT_AUTH_TENANT,
    method: 'put',
    data: data
  })
}

/** 修改租户状态 */
export function editStatusTenantApi(id, status) {
  return request({
    url: Api.EDIT_STATUS_TENANT,
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除租户 */
export function delTenantApi(ids) {
  return request({
    url: Api.DEL_BATCH_TENANT + ids.toString(),
    method: 'delete'
  })
}
