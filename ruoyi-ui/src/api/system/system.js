import request from '@/utils/request'
import defaultSettings from '@/settings'

//系统编号
const { systemNum } = defaultSettings

// 查询首页可展示子系统模块列表
export function viewListSystem(query) {
  return request({
    url: '/system/system/viewList',
    method: 'get',
    params: query
  })
}

// 查询子系统模块列表
export function listSystem(query) {
  return request({
    url: '/system/system/list',
    method: 'get',
    params: query
  })
}

// 查询子系统模块详细
export function getSystem(systemId) {
  return request({
    url: '/system/system/' + systemId,
    method: 'get'
  })
}

// 查询系统-菜单权限树结构
export function roleSystemMenuTreeSelect() {
  return request({
    url: '/system/system/roleSystemMenuTreeSelect/'+systemNum,
    method: 'get'
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
export function changeSystemStatus(systemId, status) {
  const data = {
    systemId,
    status
  }
  return request({
    url: '/system/system/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除子系统模块
export function delSystem(systemId) {
  return request({
    url: '/system/system/' + systemId,
    method: 'delete'
  })
}


