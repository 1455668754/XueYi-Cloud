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

    /**
     * 根据角色Id查询角色信息集合 | 角色级
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 企业Id
     * @return 角色信息集合
     */
    public Set<Long> selectRoleListByRoleId(SysRole role);

    /**
     * 根据角色Id增加角色与模块-菜单关联 | 全选型
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id | dataScope 数据范围 | wholeIds 全选模块-菜单
     */
    public void insertWholeIdsMenuScope(SysRole role);

    /**
     * 根据角色Id增加角色与模块-菜单关联 | 半选型
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id | dataScope 数据范围 | halfIds 半选模块-菜单
     */
    public void insertHalfIdsMenuScope(SysRole role);

    /**
     * 根据角色Id删除角色与模块-菜单关联
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public void deleteSystemMenuByRoleId(SysRole role);

    /**
     * 根据角色Ids组批量删除角色与模块-菜单关联
     *
     * @param role 角色信息 | enterpriseId 租户Id | params.Ids 需要删除的角色Ids组
     */
    public void deleteSystemMenuByRoleIds(SysRole role);

    /**
     * 根据角色Id增加角色与部门-岗位关联
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id | deptPostIds 部门-岗位组
     */
    public void insertDeptPostScope(SysRole role);

    /**
     * 根据角色Id删除角色与部门-岗位关联
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public void deleteDeptPostByRoleId(SysRole role);

    /**
     * 根据角色Ids组批量删除角色与部门-岗位关联
     *
     * @param role 角色信息 | enterpriseId 租户Id | params.Ids 需要删除的角色Ids组
     */
    public void deleteDeptPostByRoleIds(SysRole role);
}
