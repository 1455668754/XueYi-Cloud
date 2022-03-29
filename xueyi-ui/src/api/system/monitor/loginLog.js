import request from '@/utils/request'

/** 查询系统访问记录列表 */
export function listLoginLogApi(query) {
  return request({
    url: '/system/loginLog/list',
    method: 'get',
    params: query
  })
}

/** 删除系统访问记录 */
export function delLoginLogApi(ids) {
  return request({
    url: '/system/loginLog/batch/' + ids.toString(),
    method: 'delete'
  })
}

/** 清空系统访问记录 */
export function cleanLoginLogApi(ids) {
  return request({
    url: '/system/loginLog/clean',
    method: 'delete'
  })
}
