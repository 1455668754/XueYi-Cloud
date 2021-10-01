package com.xueyi.system.authority.service;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SystemMenu;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限管理 业务层
 *
 * @author xueyi
 */
public interface ISysAuthorityService {

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    public List<SysRole> selectRoleListByTenantId(SysRole role);

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    public List<SysRole> selectRoleListByEnterpriseId(SysRole role);

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    public List<SysRole> selectRoleListByDeptId(SysRole role);

    /**
     * 根据岗位Id查询角色信息集合
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    public List<SysRole> selectRoleListByPostId(SysRole role);

    /**
     * 根据用户Id查询角色信息集合
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    public List<SysRole> selectRoleListByUserId(SysRole role);

    /**
     * 根据用户Id查询角色信息集合
     *
     * @param roles 角色信息集合
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    public Map<String, Set<SystemMenu>> assembleSystemMenuSet(List<SysRole> roles);

    /**
     * 装配菜单集合
     *
     * @param roles 角色信息集合
     * @return 菜单集合
     */
    public Set<SysMenu> selectSystemMenuSet(List<SysRole> roles);
}
