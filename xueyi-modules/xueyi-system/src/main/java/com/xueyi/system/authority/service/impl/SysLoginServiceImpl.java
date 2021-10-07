package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录验证Service业务层处理
 *
 * @author xueyi
 */
@Service
@DS("main")
public class SysLoginServiceImpl implements ISysLoginService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private ISysLoginService loginService;

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    @Override
    @DS("#user.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SysUser checkLoginByEnterpriseIdANDUserName(SysUser user) {
        return userMapper.checkLoginByEnterpriseIdANDUserName(user);
    }

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param sourceName   数据源名称
     * @param roleList     角色信息集合 | roleId 角色Id
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(String sourceName, Set<Long> roleList, String userType, Long enterpriseId) {
        Set<String> roles = new HashSet<>();
        // 租管租户拥有所有权限
        if(EnterpriseUtils.isAdminTenant(enterpriseId)){
            roles.add("administrator");
        }
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            roles.add("admin");
        } else {
            List<SysRole> roleListCache = AuthorityUtils.getRoleListCache(enterpriseId, roleList);
            roles.addAll(roleListCache.stream().filter(role -> StringUtils.isNotEmpty(role.getRoleKey()) && StringUtils.equals(Constants.STATUS_NORMAL,role.getStatus())).map(SysRole::getRoleKey).collect(Collectors.toSet()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限（登录校验）
     *
     * @param menu       菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @param userType   用户标识
     * @param sourceName 数据源名称
     * @return 菜单权限信息
     */
    @Override
    @DS("#sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<String> getMenuPermission(String sourceName, SysMenu menu, String userType) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            perms.add("*:*:*");
        } else {
            menu.getParams().put("roleSystemPerms", roleSystemMenuMapper.selectSystemMenuListByUserId(menu));
            perms.addAll(loginService.checkLoginMenuPerms(menu));
        }
        return perms;
    }

    /**
     * 根据用户Id查询权限（登录校验）
     *
     * @param menu 菜单信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
