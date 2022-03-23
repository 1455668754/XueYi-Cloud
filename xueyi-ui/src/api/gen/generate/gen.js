import request from '@/utils/request'

/** 查询业务表列表 */
export function listGenApi(query) {
  return request({
    url: '/code/gen/list',
    method: 'get',
    params: query
  })
}

/** 查询生成表的字段列表 */
export function listGenColumnApi(tableId) {
  return request({
    url: '/code/gen/column/' + tableId,
    method: 'get'
  })
}

/** 查询数据库表列表 */
export function listDBGenApi(query) {
  return request({
    url: '/code/gen/db/list',
    method: 'get',
    params: query
  })
}

/** 查询生成表配置详细 */
export function getGenApi(id) {
  return request({
    url: '/code/gen/' + id,
    method: 'get'
  })
}

/** 查询生成表配置详细 | 带子数据 */
export function getSubGenApi(id) {
  return request({
    url: '/code/gen/sub/' + id,
    method: 'get'
  })
}

/** 预览代码 */
export function previewGenApi(tableId) {
  return request({
    url: '/code/gen/preview/' + tableId,
    method: 'get'
  })
}

/** 生成代码（下载方式） */
export function downloadGenApi(tableId) {
  return request({
    url: '/code/gen/download/' + tableId,
    method: 'get'
  })
}

/** 生成代码（自定义路径） */
export function generateGenApi(tableId) {
  return request({
    url: '/code/gen/generate/' + tableId,
    method: 'get'
  })
}

/** 导入数据表 */
export function importDBGenApi(names) {
  return request({
    url: '/code/gen/importTable',
    method: 'post',
    params: { tables: names.toString() }
  })
}

/** 修改数据表 */
export function editGenApi(data) {
  return request({
    url: '/code/gen',
    method: 'put',
    data: data
  })
}

/** 强制删除业务表 */
export function delForceGenApi(ids) {
  return request({
    url: '/code/gen/batch/force/' + ids,
    method: 'delete'
  })
}
