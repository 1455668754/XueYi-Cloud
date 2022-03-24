import request from '@/utils/request'

const Api = {
  LIST_GEN: '/code/gen/list',
  LIST_DB_GEN: '/code/gen/db/list',
  LIST_GEN_COLUMN: '/code/gen/column/',
  PREVIEW_GEN: '/code/gen/preview/',
  DOWNLOAD_GEN: '/code/gen/download/',
  GENERATE_GEN: '/code/gen/generate/',
  GET_GEN: '/code/gen/',
  GET_SUB_GEN: '/code/gen/sub/',
  IMPORT_DB_GEN: '/code/gen/importTable',
  EDIT_GEN: '/code/gen',
  DEL_BATCH_FORCE_DEPT: '/code/gen/batch/force/'
}

/** 查询业务表列表 */
export function listGenApi(query) {
  return request({
    url: Api.LIST_GEN,
    method: 'get',
    params: query
  })
}

/** 查询生成表的字段列表 */
export function listGenColumnApi(tableId) {
  return request({
    url: Api.LIST_GEN_COLUMN + tableId,
    method: 'get'
  })
}

/** 查询数据库表列表 */
export function listDBGenApi(query) {
  return request({
    url: Api.LIST_DB_GEN,
    method: 'get',
    params: query
  })
}

/** 查询生成表配置详细 */
export function getGenApi(id) {
  return request({
    url: Api.GET_GEN + id,
    method: 'get'
  })
}

/** 查询生成表配置详细 | 带子数据 */
export function getSubGenApi(id) {
  return request({
    url: Api.GET_SUB_GEN + id,
    method: 'get'
  })
}

/** 预览代码 */
export function previewGenApi(tableId) {
  return request({
    url: Api.PREVIEW_GEN + tableId,
    method: 'get'
  })
}

/** 生成代码（下载方式） */
export function downloadGenApi(tableId) {
  return request({
    url: Api.DOWNLOAD_GEN + tableId,
    method: 'get'
  })
}

/** 生成代码（自定义路径） */
export function generateGenApi(tableId) {
  return request({
    url: Api.GENERATE_GEN + tableId,
    method: 'get'
  })
}

/** 导入数据表 */
export function importDBGenApi(names) {
  return request({
    url: Api.IMPORT_DB_GEN,
    method: 'post',
    params: { tables: names.toString() }
  })
}

/** 修改数据表 */
export function editGenApi(data) {
  return request({
    url: Api.EDIT_GEN,
    method: 'put',
    data: data
  })
}

/** 强制删除业务表 */
export function delForceGenApi(ids) {
  return request({
    url: Api.DEL_BATCH_FORCE_DEPT + ids,
    method: 'delete'
  })
}
