package com.xueyi.system.authority.service;

import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.organize.SysUser;

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
     * @param enterpriseName 企业账号
     * @return 租户对象
     */
    public SysEnterprise checkLoginByEnterpriseName(String enterpriseName);

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userName     用户账号
     * @return 用户对象信息
     */
    public SysUser checkLoginByEnterpriseIdANDUserName(Long enterpriseId, String userName);

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param deptId       部门Id
     * @param postId       岗位Id
     * @param userId       用户Id
     * @param userType     用户标识
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(Long enterpriseId, Long deptId, Long postId, Long userId, String userType);

    /**
     * 获取菜单数据权限（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userId       用户Id
     * @param userType     用户标识
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(Long enterpriseId, Long userId, String userType);
}
