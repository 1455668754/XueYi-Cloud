import request from '@/utils/request'
import {uploadAvatar} from "@/api/system/user";

// 查询Logo
export function getLogo() {
  return request({
    url: '/system/enterprise/logo',
    method: 'get',
  })
}

// 查询企业信息
export function getEnterpriseProfile() {
  return request({
    url: '/system/enterprise/profile',
    method: 'get',
  })
}

// 用户头像上传
export function uploadLogo(data) {
  return request({
    url: '/system/enterprise/changeLogo',
    method: 'post',
    data: data
  })
}
