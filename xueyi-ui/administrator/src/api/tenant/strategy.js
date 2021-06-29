import request from '@/utils/request'

// 查询数据源策略列表
export function listStrategy(query) {
  return request({
    url: '/tenant/strategy/list',
    method: 'get',
    params: query
  })
}

// 查询数据源策略详细
export function getStrategy(query) {
  return request({
    url: '/tenant/strategy/byId',
    method: 'get',
    params: query
  })
}

// 查询数据源策略列表（排除停用）
export function listStrategyExclude(query) {
  return request({
    url: '/tenant/strategy/exclude',
    method: 'get',
    params: query
  })
}

// 新增数据源策略
export function addStrategy(data) {
  return request({
    url: '/tenant/strategy',
    method: 'post',
    data: data
  })
}

// 修改数据源策略
export function updateStrategy(data) {
  return request({
    url: '/tenant/strategy',
    method: 'put',
    data: data
  })
}

// 修改数据源策略排序
export function updateStrategySort(data) {
  return request({
    url: '/tenant/strategy/sort',
    method: 'put',
    data: data
  })
}

// 删除数据源策略
export function delStrategy(data) {
  return request({
    url: '/tenant/strategy',
    method: 'delete',
    data: data
  })
}
