import request from '@/utils/request'

// 查询Nacos配置列表
export function listNacos(query) {
  return request({
    url: '/tenant/nacos/list',
    method: 'get',
    params: query
  })
}


// 查询Nacos配置详细
export function getNacos(query) {
  return request({
    url: '/tenant/nacos/byId',
    method: 'get',
    params: query
  })
}

// 新增Nacos配置
export function addNacos(data) {
  return request({
    url: '/tenant/nacos',
    method: 'post',
    data: data
  })
}

// 修改Nacos配置
export function updateNacos(data) {
  return request({
    url: '/tenant/nacos',
    method: 'put',
    data: data
  })
}

// 修改Nacos配置排序
export function updateNacosSort(data) {
  return request({
    url: '/tenant/nacos/sort',
    method: 'put',
    data: data
  })
}

// 删除Nacos配置
export function delNacos(data) {
  return request({
    url: '/tenant/nacos',
    method: 'delete',
    data: data
  })
}
