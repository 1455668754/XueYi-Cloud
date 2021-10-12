import request from '@utils/request'

// 查询子系统模块列表
export function listSystem(query) {
  return request({
    url: '/system/system/list',
    method: 'get',
    params: query
  })
}

// 查询子系统模块详细
export function getSystem(query) {
  return request({
    url: '/system/system/byId',
    method: 'get',
    params: query
  })
}

// 新增子系统模块
export function addSystem(data) {
  return request({
    url: '/system/system',
    method: 'post',
    data: data
  })
}

// 修改子系统模块
export function updateSystem(data) {
  return request({
    url: '/system/system',
    method: 'put',
    data: data
  })
}

// 子系统模块状态修改
export function changeSystemStatus(data) {
  return request({
    url: '/system/system/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除子系统模块
export function delSystem(data) {
  return request({
    url: '/system/system/',
    method: 'delete',
    data: data
  })
}
