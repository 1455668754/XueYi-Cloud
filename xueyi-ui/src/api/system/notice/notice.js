import request from '@/utils/request'

/** 查询通知公告列表 */
export function listNoticeApi(query) {
  return request({
    url: '/system/notice/list',
    method: 'get',
    params: query
  })
}

/** 查询通知公告选择框列表 */
export function optionNoticeApi() {
  return request({
    url: '/system/notice/option',
    method: 'get'
  })
}

/** 查询通知公告详细 */
export function getNoticeApi(id) {
  return request({
    url: '/system/notice/' + id,
    method: 'get'
  })
}

/** 新增通知公告 */
export function addNoticeApi(data) {
  return request({
    url: '/system/notice',
    method: 'post',
    data: data
  })
}

/** 修改通知公告 */
export function editNoticeApi(data) {
  return request({
    url: '/system/notice',
    method: 'put',
    data: data
  })
}

/** 修改通知公告状态 */
export function editStatusNoticeApi(id, status) {
  return request({
    url: '/system/notice/status',
    method: 'put',
    data: { id: id, status: status }
  })
}

/** 删除通知公告 */
export function delNoticeApi(ids) {
  return request({
    url: '/system/notice/batch/' + ids.toString(),
    method: 'delete'
  })
}


