package com.xueyi.system.authority.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;

/**
 * 角色表 数据层
 *
 * @author xueyi
 */
public interface SysRoleMapper {

    /**
     * 根据用户Id查询角色权限信息（登录校验）
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param role 角色信息 | params.deptId 部门Id | params.postId 岗位Id | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public List<SysRole> checkLoginRolePermission(SysRole role);

    /**
     * 查询所有角色 | exclude 衍生角色
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | null
     * @return 角色列表
     */
    @DataScope(eAlias = "r")
    public List<SysRole> selectRoleAll(SysRole role);

    /**
     * 根据用户Id查询角色
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | params.userName 用户名
     * @return 角色列表
     */
    @DataScope(eAlias = "r")
    public List<SysRole> selectRolesByUserName(SysRole role);

    /**
     * 根据条件分页查询角色数据
     * 访问控制 r 租户查询
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @DataScope(eAlias = "r")
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 通过角色Id查询角色
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 角色对象信息
     */
    @DataScope(eAlias = "r")
    public SysRole selectRoleById(SysRole role);

    /**
     * 新增角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     *
     * @param role 角色信息 | roleId 角色Id | status 角色状态
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateRoleStatus(SysRole role);

    /**
     * 修改角色数据范围
     *
     * @param role 角色信息 | roleId 角色Id | dataScope 数据范围
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateRoleDataScope(SysRole role);

    /**
     * 通过角色Id删除角色
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleById(SysRole role);

    /**
     * 批量删除角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleByIds(SysRole role);

    /**
     * 依据角色类型 && 衍生Id 删除角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | type 角色类型 | deriveId 衍生Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleByTypeAndDeriveId(SysRole role);

    /**
     * 依据角色类型 && 衍生Ids 批量删除角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息 | type 角色类型 | params.Ids 需要删除的衍生Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleByTypeAndDeriveIds(SysRole role);

    /**
     * 校验角色编码是否唯一
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | roleCode 角色编码
     * @return 角色信息
     */
    @DataScope(eAlias = "r")
    public SysRole checkRoleCodeUnique(SysRole role);

    /**
     * 校验角色名称是否唯一
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | roleName 角色名称
     * @return 角色信息
     */
    @DataScope(eAlias = "r")
    public SysRole checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     * 访问控制 r 租户查询
     *
     * @param role 角色信息 | roleKey 角色权限
     * @return 角色信息
     */
    @DataScope(eAlias = "r")
    public SysRole checkRoleKeyUnique(SysRole role);
}
