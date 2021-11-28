package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ISysAuthorityService authorityService;

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    @Override
    @DS("#user.sourceName")
    public SysUser checkLoginByEnterpriseIdANDUserName(SysUser user) {
        return userMapper.checkLoginByEnterpriseIdANDUserName(user);
    }

    /**
     * 获取角色数据权限（登录校验）
     *
     * @param roleList     角色信息集合 | roleId 角色Id
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(Set<Long> roleList, String userType, Long enterpriseId) {
        Set<String> roles = new HashSet<>();
        // 租管租户拥有所有权限
        if(EnterpriseUtils.isAdminTenant(enterpriseId)){
            roles.add("administrator");
        }
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            roles.add("admin");
        } else {
            List<SysRole> roleListCache = authorityService.getRoleAuthorityBySourceName(enterpriseId, roleList, DataSourceUtils.getMainSourceNameByEnterpriseId(enterpriseId));
            roles.addAll(roleListCache.stream().filter(role -> StringUtils.isNotEmpty(role.getRoleKey()) && StringUtils.equals(BaseConstants.Status.NORMAL.getCode(),role.getStatus())).map(SysRole::getRoleKey).collect(Collectors.toSet()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限（登录校验）
     *
     * @param roleList     角色信息集合 | roleId 角色Id
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(Set<Long> roleList, String userType, Long enterpriseId) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userType)) {
            perms.add("*:*:*");
        } else {
            Set<SysMenu> menuSet = authorityService.selectMenuSet(enterpriseId, authorityService.getRoleAuthorityBySourceName(enterpriseId, roleList, DataSourceUtils.getMainSourceNameByEnterpriseId(enterpriseId)),EnterpriseUtils.isAdminTenant(enterpriseId), true,true);
            Set<SysMenu> ownerMenuSet = AuthorityUtils.getMenuCache(enterpriseId);
            ownerMenuSet.retainAll(menuSet);
            perms.addAll(ownerMenuSet.stream().filter(menu -> StringUtils.isNotEmpty(menu.getPerms()) && StringUtils.equals(BaseConstants.Status.NORMAL.getCode(),menu.getStatus())).map(SysMenu::getPerms).collect(Collectors.toSet()));
        }
        return perms;
    }
}
