package com.xueyi.system.role.mapper;

import java.util.List;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.role.domain.SysOrganizeRole;

/**
 * 组织和角色关联 数据层
 *
 * @author xueyi
 */
public interface SysOrganizeRoleMapper {

    /**
     * 查询组织和角色关联列表
     * 访问控制 e 租户查询
     *
     * @param organizeRole 组织和角色关联信息
     * @return 部门和角色关联集合
     */
    @DataScope( eAlias = "e" )
    public List<SysOrganizeRole> selectOrganizeRoleList(SysOrganizeRole organizeRole);

    /**
     * 查询组织和角色关联
     * 访问控制 e 租户查询
     *
     * @param organizeRole 组织和角色关联信息 | deptId 部门和角色关联Id
     * @return 部门和角色关联
     */
    @DataScope( eAlias = "e" )
    public SysOrganizeRole selectOrganizeRoleByDeptId(SysOrganizeRole organizeRole);

    /**
     * 新增组织和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param organizeRole 组织和角色关联信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int insertOrganizeRole(SysOrganizeRole organizeRole);

    /**
     * 删除组织和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteOrganizeRoleByRoleId(SysRole role);

    /**
     * 批量删除组织和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteOrganizeRoleByRoleIds(SysRole role);

    /**
     * 删除组织和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param organizeRole 组织和角色关联信息 | deptId 部门Id | postId 岗位Id | userId 用户Id | deriveEnterpriseId 租户Id （控制类型，执行删除操作，删除 值不为空控制的类型 && = 控制对应类型值 的值，must）
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteOrganizeRoleByOrganizeId(SysOrganizeRole organizeRole);

    /**
     * 批量删除组织和角色关联
     * 访问控制 empty 租户更新（无前缀）
     * @param organizeRole 组织和角色关联信息 | deptId 部门Id | postId 岗位Id | userId 用户Id | deriveEnterpriseId 租户Id （控制类型，执行删除操作，删除 值不为空控制的类型 && in params.Ids中 的值，must）
     * @param organizeRole 组织和角色关联信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteOrganizeRoleByOrganizeIds(SysOrganizeRole organizeRole);
}