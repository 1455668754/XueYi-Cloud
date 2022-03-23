import request from '@/utils/request'

/** 查询字典类型选择框列表 */
export function optionDictTypeApi() {
  return request({
    url: '/system/dict/type/option',
    method: 'get'
  })
}

