import request from '@/utils/request'

/** 获取企业部门|岗位树 */
export function organizeScopeApi(query) {
  return request({
    url: '/system/organize/organizeScope',
    method: 'get',
    params: query
  })
}

/** 获取企业部门|岗位树 | 无部门叶子节点 */
export function organizeOptionApi() {
  return request({
    url: '/system/organize/option',
    method: 'get'
  })
}
