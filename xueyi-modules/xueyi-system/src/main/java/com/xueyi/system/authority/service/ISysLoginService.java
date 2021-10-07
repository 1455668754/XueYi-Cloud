package com.xueyi.system.authority.service;

import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.organize.SysUser;

import java.util.Set;

/**
 * 登录验证 业务层
 *
 * @author xueyi
 */
public interface ISysLoginService {

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
     * @param sourceName   数据源名称
     * @param roleList     角色信息集合 | roleId 角色Id
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(String sourceName, Set<Long> roleList, String userType, Long enterpriseId);

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