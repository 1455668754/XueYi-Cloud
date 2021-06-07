import request from '@/utils/request'

// 查询数据源列表
export function listSeparation(query) {
  return request({
    url: '/tenant/separation/list',
    method: 'get',
    params: query
  })
}


// 查询数据源详细
export function getSeparation(query) {
  return request({
    url: '/tenant/separation/byId',
    method: 'get',
    params: query
  })
}

// 修改数据源
export function updateSeparation(data) {
  return request({
    url: '/tenant/separation',
    method: 'put',
    data: data
  })
}

// 查询 含读 数据源集合
export function readSeparation(query) {
  return request({
    url: '/tenant/separation/containRead',
    method: 'get',
    params: query
  })
}
