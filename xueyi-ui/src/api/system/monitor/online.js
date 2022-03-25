import request from '@/utils/request'

/** 查询在线用户列表 */
export function listOnlineApi(query) {
  return request({
    url: '/system/online/list',
    method: 'get',
    params: query
  })
}

/** 强退在线用户 */
export function delOnlineApi(ids) {
  return request({
    url: '/system/online/batch/' + ids.toString(),
    method: 'delete'
  })
}
