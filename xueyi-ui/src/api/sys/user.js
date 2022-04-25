import request from '@/utils/request'

/** 查询用户个人信息 */
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

/** 修改用户个人信息 */
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

/** 用户密码重置 */
export function updateUserPwd(oldPassword, newPassword) {
  return request({
    url: '/system/user/profile/password',
    method: 'put',
    params: { oldPassword, newPassword }
  })
}

/** 用户头像上传 */
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

