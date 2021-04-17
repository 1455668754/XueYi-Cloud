package com.xueyi.system.authority.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.utilTool.SysSearch;

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
     * @param search 万用组件 | userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public List<SysRole> checkLoginRolePermission(SysSearch search);

    /**
     * 查询所有角色
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | null
     * @return 角色列表
     */
    @DataScope(enterpriseAlias = "r")
    public List<SysRole> selectRoleAll(SysSearch search);

    /**
     * 根据用户Id查询角色
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | userName 用户名
     * @return 角色列表
     */
    @DataScope(enterpriseAlias = "r")
    public List<SysRole> selectRolesByUserName(SysSearch search);


    /**
     * 根据条件分页查询角色数据
     * 访问控制 r 部门 | r 租户查询
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @DataScope(deptAlias = "r", enterpriseAlias = "r")
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 通过角色Id查询角色
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 角色对象信息
     */
    @DataScope(enterpriseAlias = "r")
    public SysRole selectRoleById(SysSearch search);

    /**
     * 新增角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int insertRole(SysRole role);

    /**
     * 修改角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param role 角色信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     *
     * @param search 万用组件 | roleId 角色Id | status 角色状态
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateRoleStatus(SysSearch search);

    /**
     * 修改角色数据范围
     *
     * @param search 万用组件 | roleId 角色Id | dataScope 数据范围
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateRoleDataScope(SysSearch search);

    /**
     * 通过角色Id删除角色
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleById(SysSearch search);

    /**
     * 批量删除角色信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleIds 需要删除的角色Id(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleByIds(SysSearch search);

    /**
     * 校验角色编码是否唯一
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | roleCode 角色编码
     * @return 角色信息
     */
    @DataScope(enterpriseAlias = "r")
    public SysRole checkRoleCodeUnique(SysSearch search);

    /**
     * 校验角色名称是否唯一
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | roleName 角色名称
     * @return 角色信息
     */
    @DataScope(enterpriseAlias = "r")
    public SysRole checkRoleNameUnique(SysSearch search);

    /**
     * 校验角色权限是否唯一
     * 访问控制 r 租户查询
     *
     * @param search 万用组件 | roleKey 角色权限
     * @return 角色信息
     */
    @DataScope(enterpriseAlias = "r")
    public SysRole checkRoleKeyUnique(SysSearch search);
}
