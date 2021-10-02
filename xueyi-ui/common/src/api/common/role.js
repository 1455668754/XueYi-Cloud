import request from '../../utils/request'

// 角色菜单权限获取
export function getTenantMenuScope(tenantId) {
    return request({
        url: '/system/authority/tenantScope/' + tenantId,
        method: 'get'
    })
}
