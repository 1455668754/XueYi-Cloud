import request from '@utils/request'

// 查询租户信息列表
export function listTenant(query) {
  return request({
    url: '/tenant/tenant/list',
    method: 'get',
    params: query
  })
}

// 查询租户信息详细
export function getTenant(query) {
  return request({
    url: '/tenant/tenant/byId',
    method: 'get',
    params: query
  })
}

// 新增租户信息
export function addTenant(data) {
  return request({
    url: '/tenant/tenant',
    method: 'post',
    data: data
  })
}

// 修改租户信息
export function updateTenant(data) {
  return request({
    url: '/tenant/tenant',
    method: 'put',
    data: data
  })
}

// 修改租户信息排序
export function updateTenantSort(data) {
  return request({
    url: '/tenant/tenant/sort',
    method: 'put',
    data: data
  })
}

// 删除租户信息
export function delTenant(data) {
  return request({
    url: '/tenant/tenant',
    method: 'delete',
    data: data
  })
}
