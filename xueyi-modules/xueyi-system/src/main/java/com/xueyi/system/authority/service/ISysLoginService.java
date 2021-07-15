package com.xueyi.system.authority.service;

import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.domain.SysMenu;

import java.util.Set;

/**
 * 登录验证 业务层
 *
 * @author xueyi
 */
public interface ISysLoginService {

    /**
     * 通过企业账号查询租户
     *
     * @param enterprise 租户对象 | enterpriseName 企业账号
     * @return 租户对象
     */
    public SysEnterprise checkLoginByEnterpriseName(SysEnterprise enterprise);

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    public SysUser checkLoginByEnterpriseIdANDUserName(SysUser user);

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param role       角色信息 | params.deptId 部门Id | params.postId 岗位Id | params.userId 用户Id | enterpriseId 租户Id
     * @param userType   用户标识
     * @param sourceName 数据源名称
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(String sourceName, SysRole role, String userType);

    /**
     * 根据用户Id查询角色（登录校验）
     *
     * @param role 角色信息 | params.deptId 部门Id | params.postId 岗位Id | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public Set<String> checkLoginRolePerms(SysRole role);

    /**
     * 获取菜单数据权限（登录校验）
     *
     * @param menu       菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @param userType   用户标识
     * @param sourceName 数据源名称
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(String sourceName, SysMenu menu, String userType);

    /**
     * 根据用户Id查询权限（登录校验）
     *
     * @param menu 菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public Set<String> checkLoginMenuPerms(SysMenu menu);
}