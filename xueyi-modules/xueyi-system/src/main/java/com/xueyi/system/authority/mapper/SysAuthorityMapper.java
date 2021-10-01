package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;

import java.util.Set;

/**
 * 权限管理 数据层
 *
 * @author xueyi
 */
public interface SysAuthorityMapper {
    
    /**
     * 根据租户Id查询角色集合
     *
     * @param role 角色信息 | enterpriseId 租户Id
     * @return 角色集合
     */
    @DataScope(eAlias = "sor")
    public Set<Long> selectRoleListByTenantId(SysRole role);

    /**
     * 根据企业Id查询角色集合
     *
     * @param role 角色信息 | enterpriseId 租户Id
     * @return 角色集合
     */
    @DataScope(eAlias = "sor")
    public Set<Long> selectRoleListByEnterpriseId(SysRole role);

    /**
     * 根据部门Id查询角色集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色集合
     */
    @DataScope(eAlias = "sor")
    public Set<Long> selectRoleListByDeptId(SysRole role);

    /**
     * 根据岗位Id查询角色集合
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 租户Id
     * @return 角色集合
     */
    @DataScope(eAlias = "sor")
    public Set<Long> selectRoleListByPostId(SysRole role);

    /**
     * 根据用户Id查询角色集合
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 角色集合
     */
    @DataScope(eAlias = "sor")
    public Set<Long> selectRoleListByUserId(SysRole role);
}
