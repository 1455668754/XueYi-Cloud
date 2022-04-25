import request from '@/utils/request'

/** 参数查询 */
export function dicConfig(code) {
  return request({
    url: '/system/config/configKey/' + code,
    method: 'get'
  })
}

/** 字典查询 */
export function dicDict(code) {
  return request({
    url: '/system/dict/data/type/' + code,
    method: 'get'
  })
}

/** 字典批量查询 */
export function dicDictList(dictCodeList) {
  return request({
    url: '/system/dict/data/types/' + dictCodeList.toString(),
    method: 'get'
  })
}
