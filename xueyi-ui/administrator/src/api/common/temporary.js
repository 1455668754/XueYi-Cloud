import request from '@/utils/request'

//借用接口

// 查询系统-菜单树结构
export function treeSelect(query) {
  return request({
    url: '/system/system/roleSystemMenuTreeSelect',
    method: 'get',
    params: query
  })
}
