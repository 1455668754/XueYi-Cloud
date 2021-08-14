import request from '../../utils/request'

// 素材上传
export function uploadMaterial(data) {
  return request({
    url: '/system/material/upload',
    method: 'post',
    data: data
  })
}

// 查询素材信息列表
export function listMaterial(query) {
  return request({
    url: '/system/material/list',
    method: 'get',
    params: query
  })
}

// 修改素材信息
export function updateMaterial(data) {
  return request({
    url: '/system/material',
    method: 'put',
    data: data
  })
}

// 删除素材信息
export function delMaterial(materialId) {
  return request({
    url: '/system/material/' + materialId,
    method: 'delete'
  })
}

// 查询父级分类信息
export function parentFolder(query) {
  return request({
    url: '/system/folder/parent',
    method: 'get',
    params: query
  })
}

// 新增素材分类
export function addFolder(data) {
  return request({
    url: '/system/folder',
    method: 'post',
    data: data
  })
}

// 修改素材分类
export function updateFolder(data) {
  return request({
    url: '/system/folder',
    method: 'put',
    data: data
  })
}

// 删除素材分类
export function delFolder(folderId) {
  return request({
    url: '/system/folder/' + folderId,
    method: 'delete'
  })
}
