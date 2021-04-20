import request from '@/utils/request'

// 查询岗位列表
export function listPost(query) {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

// 修改岗位-角色关系
export function changePostRole(data) {
  return request({
    url: '/system/post/changePostRole',
    method: 'put',
    data: data
  })
}

// 岗位状态修改
export function changePostStatus(postId, deptId, status) {
  const data = {
    postId,
    deptId,
    status
  }
  return request({
    url: '/system/post/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}
