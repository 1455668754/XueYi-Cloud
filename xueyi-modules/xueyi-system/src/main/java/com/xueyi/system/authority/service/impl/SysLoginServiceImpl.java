package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录验证Service业务层处理
 *
 * @author xueyi
 */
@Service
public class SysLoginServiceImpl implements ISysLoginService {
    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;
    /**
     * 通过企业账号查询租户信息（登录校验）
     *
     * @param enterprise 租户对象 | enterpriseName 企业账号
     * @return 租户对象信息
     */
    @Override
    public SysEnterprise checkLoginByEnterpriseName(SysEnterprise enterprise) {
        return enterpriseMapper.checkLoginByEnterpriseName(enterprise);
    }

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @param sourceName   数据源名称
     * @return 用户对象信息
     */
    @Override
    @DS("#sourceName")
    public SysUser checkLoginByEnterpriseIdANDUserName(String sourceName, SysUser user) {
        return userMapper.checkLoginByEnterpriseIdANDUserName(user);
    }

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param role 角色信息 | params.deptId 部门Id | params.postId 岗位Id | params.userId 用户Id | enterpriseId 租户Id
     * @param userType     用户标识
     * @param sourceName   数据源名称
     * @return 角色权限信息
     */
    @Override
    @DS("#sourceName")
    public Set<String> getRolePermission(String sourceName, SysRole role, String userType) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            roles.add("admin");
        } else {
            roles.addAll(checkLoginRolePerms(role));
        }
        return roles;
    }

    /**
     * 根据用户Id查询角色（登录校验）
     *
     * @param role 角色信息 | params.deptId 部门Id | params.postId 岗位Id | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public Set<String> checkLoginRolePerms(SysRole role) {
        List<SysRole> perms = roleMapper.checkLoginRolePermission(role);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 获取菜单数据权限（登录校验）
     *
     * @param menu 菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @param userType     用户标识
     * @param sourceName   数据源名称
     * @return 菜单权限信息
     */
    @Override
    @DS("#sourceName")
    public Set<String> getMenuPermission(String sourceName, SysMenu menu, String userType) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            perms.add("*:*:*");
        } else {
            SysSearch search = new SysSearch();
            search.setEnterpriseId(menu.getEnterpriseId());
            search.getSearch().put("userId", menu.getParams().get("userId"));
            List<Long> roleSystemPerms = roleSystemMenuMapper.selectSystemMenuPermsList(search);
            menu.getParams().put("roleSystemPerms", roleSystemPerms);
            perms.addAll(checkLoginMenuPerms(menu));
        }
        return perms;
    }

    /**
     * 根据用户Id查询权限（登录校验）
     *
     * @param menu 菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public Set<String> checkLoginMenuPerms(SysMenu menu) {
        List<String> perms = menuMapper.checkLoginMenuPermission(menu);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}
