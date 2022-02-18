import request from '@utils/request'

// 查询数据源列表
export function listSource(query) {
  return request({
    url: '/tenant/source/list',
    method: 'get',
    params: query
  })
}


// 查询数据源详细
export function getSource(query) {
  return request({
    url: '/tenant/source/byId',
    method: 'get',
    params: query
  })
}

// 数据源连接测试
export function connectionSource(data) {
  return request({
    url: '/tenant/source/connection',
    method: 'post',
    data: data
  })
}

// 新增数据源
export function addSource(data) {
  return request({
    url: '/tenant/source',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateSource(data) {
  return request({
    url: '/tenant/source',
    method: 'put',
    data: data
  })
}

// 修改数据源状态
export function updateSourceStatus(data) {
  return request({
    url: '/tenant/source/status',
    method: 'put',
    data: data
  })
}

// 修改数据源排序
export function updateSourceSort(data) {
  return request({
    url: '/tenant/source/sort',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delSource(data) {
  return request({
    url: '/tenant/source',
    method: 'delete',
    data: data
  })
}
