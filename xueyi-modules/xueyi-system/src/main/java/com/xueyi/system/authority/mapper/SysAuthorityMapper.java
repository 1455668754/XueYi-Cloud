package com.xueyi.system.authority.mapper;

import com.xueyi.system.api.domain.authority.SysRole;

import java.util.Set;

/**
 * 权限管理 数据层
 *
 * @author xueyi
 */
public interface SysAuthorityMapper {
    
    /**
     * 根据租户Id查询角色集合 | 租户级
     *
     * @param enterpriseId 租户Id
     * @return 角色集合
     */
    public Set<Long> selectRoleListByTenantId(Long enterpriseId);

    /**
     * 根据企业Id查询角色集合 | 企业级
     *
     * @param role 角色信息 | enterpriseId 租户Id
     * @return 角色集合
     */
    public Set<Long> selectRoleListByEnterpriseId(SysRole role);

    /**
     * 根据部门Id查询角色集合 | 部门级
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色集合
     */
    
    public Set<Long> selectRoleListByDeptId(SysRole role);

    /**
     * 根据岗位Id查询角色集合 | 岗位级
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 租户Id
     * @return 角色集合
     */
    
    public Set<Long> selectRoleListByPostId(SysRole role);

    /**
     * 根据用户Id查询角色集合 | 用户级
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 角色集合
     */
    
    public Set<Long> selectRoleListByUserId(SysRole role);
}
