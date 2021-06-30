package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.tenant.api.source.Source;
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

    /**
     * 通过企业账号查询租户信息（登录校验）
     *
     * @param enterpriseName 企业账号
     * @return 租户对象信息
     */
    @Override
    public SysEnterprise checkLoginByEnterpriseName(String enterpriseName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseName", enterpriseName);
        return enterpriseMapper.checkLoginByEnterpriseName(search);
    }

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userName     用户账号
     * @param sourceName   数据源名称
     * @return 用户对象信息
     */
    @Override
    @DS("#sourceName")
    public SysUser checkLoginByEnterpriseIdANDUserName(String sourceName, Long enterpriseId, String userName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseId", enterpriseId);
        search.getSearch().put("userName", userName);
        return userMapper.checkLoginByEnterpriseIdANDUserName(search);
    }

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userId       用户Id
     * @param userType     用户标识
     * @param sourceName   数据源名称
     * @return 角色权限信息
     */
    @Override
    @DS("#sourceName")
    public Set<String> getRolePermission(String sourceName, Long enterpriseId, Long deptId, Long postId, Long userId, String userType) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            roles.add("admin");
        } else {
            roles.addAll(checkLoginRolePerms(enterpriseId, deptId, postId, userId));
        }
        return roles;
    }

    /**
     * 根据用户Id查询角色（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userId       用户Id
     * @return 权限列表
     */
    public Set<String> checkLoginRolePerms(Long enterpriseId, Long deptId, Long postId, Long userId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseId", enterpriseId);
        search.getSearch().put("deptId", deptId);
        search.getSearch().put("postId", postId);
        search.getSearch().put("userId", userId);
        List<SysRole> perms = roleMapper.checkLoginRolePermission(search);
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
     * @param enterpriseId 租户Id
     * @param userId       用户Id
     * @param userType     用户标识
     * @param sourceName   数据源名称
     * @return 菜单权限信息
     */
    @Override
    @DS("#sourceName")
    public Set<String> getMenuPermission(String sourceName, Long enterpriseId, Long userId, String userType) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            perms.add("*:*:*");
        } else {
            perms.addAll(checkLoginMenuPerms(enterpriseId, userId));
        }
        return perms;
    }

    /**
     * 根据用户Id查询权限（登录校验）
     *
     * @param enterpriseId 租户Id
     * @param userId       用户Id
     * @return 权限列表
     */
    public Set<String> checkLoginMenuPerms(Long enterpriseId, Long userId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseId", enterpriseId);
        search.getSearch().put("userId", userId);
        List<String> perms = menuMapper.checkLoginMenuPermission(search);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}
