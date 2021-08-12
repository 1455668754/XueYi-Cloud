import request from '@/utils/request'

//借用接口

// 查询系统-菜单树结构 - 获取所有权限内模块&菜单 | 无衍生角色
export function treeSelectPermitAll(query) {
  return request({
    url: '/system/system/systemMenuTreePermitAll',
    method: 'get',
    params: query
  })
}

// 查询系统-菜单树结构 - 获取所有权限内模块&菜单 | 无衍生角色 | 仅公共数据
export function treeSelectPermitAllOnlyPublic(query) {
  return request({
    url: '/system/system/systemMenuTreePermitAllOnlyPublic',
    method: 'get',
    params: query
  })
}
