import request from '@/utils/request'

/** 查询模块选择框列表 */
export function optionModuleApi() {
  return request({
    url: '/system/module/option',
    method: 'get'
  })
}
