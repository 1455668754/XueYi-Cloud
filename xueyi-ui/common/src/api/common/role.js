import request from '../../utils/request'

// 模块-菜单范围获取 | 租户级
export function getTenantMenuScope(tenantId) {
    return request({
        url: '/system/authority/tenantScope/' + tenantId,
        method: 'get'
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 租户级
export function getTenantMenuRange(tenantId) {
    return request({
        url: '/system/authority/tenantRange/' + tenantId,
        method: 'get'
    })
}

// 模块-菜单范围获取 | 企业级
export function getEnterpriseMenuScope(query) {
    return request({
        url: '/system/authority/enterpriseScope',
        method: 'get',
        params: query
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 企业级
export function getEnterpriseMenuRange(query) {
    return request({
        url: '/system/authority/enterpriseRange',
        method: 'get',
        params: query
    })
}

// 模块-菜单范围获取 | 部门级
export function getDeptMenuScope(query) {
    return request({
        url: '/system/authority/deptScope',
        method: 'get',
        params: query
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 部门级
export function getDeptMenuRange(query) {
    return request({
        url: '/system/authority/deptRange',
        method: 'get',
        params: query
    })
}

// 模块-菜单范围获取 | 岗位级
export function getPostMenuScope(query) {
    return request({
        url: '/system/authority/postScope',
        method: 'get',
        params: query
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 岗位级
export function getPostMenuRange(query) {
    return request({
        url: '/system/authority/postRange',
        method: 'get',
        params: query
    })
}

// 模块-菜单范围获取 | 用户级
export function getUserMenuScope(query) {
    return request({
        url: '/system/authority/userScope',
        method: 'get',
        params: query
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 用户级
export function getUserMenuRange(query) {
    return request({
        url: '/system/authority/userRange',
        method: 'get',
        params: query
    })
}
