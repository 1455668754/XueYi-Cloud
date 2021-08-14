import request from '@utils/request'

// 企业logo上传
export function uploadLogo(data) {
  return request({
    url: '/system/enterprise/changeLogo',
    method: 'post',
    data: data
  })
}

// 企业信息更新
export function updateEnterprise(data) {
  return request({
    url: '/system/enterprise/updateEnterprise',
    method: 'put',
    data: data
  })
}

// 企业账号修改
export function changeEnterpriseName(data) {
  return request({
    url: '/system/enterprise/changeEnterpriseName',
    method: 'put',
    data: data
  })
}
