import request from '../../utils/request'

// 模块-菜单范围获取 | 租管级
export function getLessorMenuScope(tenantId) {
    return request({
        url: '/system/authority/lessorScope/' + tenantId,
        method: 'get'
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 租管级
export function getLessorMenuRange(tenantId) {
    return request({
        url: '/system/authority/lessorRange/' + tenantId,
        method: 'get'
    })
}

// 模块-菜单范围获取 | 租户级
export function getTenantMenuScope(query) {
    return request({
        url: '/system/authority/tenantScope',
        method: 'get',
        params: query
    })
}

// 模块-菜单范围更新 | 租户级
export function setTenantMenuScope(data) {
    return request({
        url: '/system/authority/tenantScopeSet',
        method: 'put',
        data: data
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 租户级
export function getTenantMenuRange(query) {
    return request({
        url: '/system/authority/tenantRange',
        method: 'get',
        params: query
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

// 模块-菜单范围更新 | 企业级
export function setEnterpriseMenuScope(data) {
    return request({
        url: '/system/authority/enterpriseScopeSet',
        method: 'put',
        data: data
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

// 模块-菜单范围更新 | 部门级
export function setDeptMenuScope(data) {
    return request({
        url: '/system/authority/deptScopeSet',
        method: 'put',
        data: data
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

// 模块-菜单范围更新 | 岗位级
export function setPostMenuScope(data) {
    return request({
        url: '/system/authority/postScopeSet',
        method: 'put',
        data: data
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

// 模块-菜单范围更新 | 用户级
export function setUserMenuScope(data) {
    return request({
        url: '/system/authority/userScopeSet',
        method: 'put',
        data: data
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

// 模块-菜单范围更新 | 角色级
export function setRoleMenuScope(data) {
    return request({
        url: '/system/authority/roleScopeSet',
        method: 'put',
        data: data
    })
}

// 获取模块-菜单选择 | halfIds 半选 | wholeIds 全选 | 角色级
export function getRoleMenuRange(query) {
    return request({
        url: '/system/authority/roleRange',
        method: 'get',
        params: query
    })
}

// 获取部门-岗位选择| deptPostIds 全选 | 角色级
export function getRoleDataRange(query) {
    return request({
        url: '/system/authority/roleDataRange',
        method: 'get',
        params: query
    })
}