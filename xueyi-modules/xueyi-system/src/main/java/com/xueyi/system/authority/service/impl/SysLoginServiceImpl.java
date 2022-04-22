package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.organize.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysLoginServiceImpl implements ISysLoginService {

    @Autowired
    ISysEnterpriseService enterpriseService;

    @Autowired
    ISysDeptService deptService;

    @Autowired
    ISysPostService postService;

    @Autowired
    ISysUserService userService;

    @Autowired
    ISysMenuService menuService;

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 登录校验 | 根据企业账号查询企业信息
     *
     * @param enterpriseName 企业账号
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto loginByEnterpriseName(String enterpriseName) {
        return enterpriseService.selectByName(enterpriseName);
    }

    /**
     * 登录校验 | 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @param password 密码
     * @return 用户对象
     */
    @Override
    public SysUserDto loginByUser(String userName, String password) {
        return userService.userLogin(userName, password);
    }

    /**
     * 登录校验 | 获取角色数据权限
     *
     * @param roleList 角色信息集合
     * @param userType 用户标识
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(List<SysRoleDto> roleList, String userType) {
        Set<String> roles = new HashSet<>();
        // 租管租户拥有租管标识权限
        if (SecurityUtils.isAdminTenant())
            roles.add("administrator");
        // 超管用户拥有超管标识权限
        if (SysUserDto.isAdmin(userType))
            roles.add("admin");
        else
            roles.addAll(roleList.stream().map(SysRoleDto::getRoleKey).filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        return roles;
    }

    /**
     * 登录校验 | 获取菜单数据权限
     *
     * @param roleIds  角色Id集合
     * @param userType 用户标识
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(Set<Long> roleIds, String userType) {
        Set<String> perms = new HashSet<>();
        // 租管租户的超管用户拥有所有权限
        if (SecurityUtils.isAdminTenant() && SysUserDto.isAdmin(userType))
            perms.add("*:*:*");
        else {
            Set<String> set = SysUserDto.isAdmin(userType)
                    ? menuService.loginPermission()
                    : menuService.loginPermission(roleIds);
            // 常规租户的超管用户拥有本租户最高权限
            perms.addAll(set.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        }
        return perms;
    }

    /**
     * 登录校验 | 获取数据数据权限
     *
     * @param roleList 角色信息集合
     * @param user     用户对象
     * @return 数据权限对象
     */
    @Override
    public DataScope getDataScope(List<SysRoleDto> roleList, SysUserDto user) {
        DataScope scope = new DataScope();
        // 1.判断是否为超管用户
        if (user.isAdmin()) {
            scope.setDataScope(AuthorityConstants.DataScope.ALL.getCode());
            return scope;
        }
        // 2.判断有无全部数据权限角色
        for (SysRoleDto role : roleList) {
            if (StrUtil.equals(role.getDataScope(), AuthorityConstants.DataScope.ALL.getCode())) {
                scope.setDataScope(AuthorityConstants.DataScope.ALL.getCode());
                return scope;
            }
        }
        // 3.组建权限集
        Set<Long> deptScope = new HashSet<>(), postScope = new HashSet<>(), userScope = new HashSet<>(), customRoleId = new HashSet<>();
        int isCustom = 0, isDept = 0, isDeptAndChild = 0, isPost = 0, isSelf = 0;
        for (SysRoleDto role : roleList) {
            switch (Objects.requireNonNull(AuthorityConstants.DataScope.getValue(role.getDataScope()))) {
                case CUSTOM:
                    isCustom++;
                    customRoleId.add(role.getId());
                    break;
                case DEPT:
                    if (isDept++ == 0)
                        deptScope.addAll(user.getPosts().stream().map(post -> post.getDept().getId()).collect(Collectors.toSet()));
                    break;
                case DEPT_AND_CHILD:
                    if (isDeptAndChild++ == 0) {
                        Set<Long> deptIds = user.getPosts().stream().map(post -> post.getDept().getId()).collect(Collectors.toSet());
                        List<SysDeptDto> deptList;
                        for (Long deptId : deptIds) {
                            deptList = deptService.selectChildListById(deptId);
                            deptScope.addAll(deptList.stream().map(SysDeptDto::getId).collect(Collectors.toSet()));
                        }
                    }
                    break;
                case POST:
                    if (isPost++ == 0)
                        postScope.addAll(user.getPosts().stream().map(SysPostDto::getId).collect(Collectors.toSet()));
                    break;
                case SELF:
                    if (isSelf++ == 0)
                        userScope.add(user.getId());
                    break;
                default:
                    break;
            }
        }

        if (isCustom > 0) {
            deptScope.addAll(organizeService.selectRoleDeptSetByRoleIds(customRoleId));
            postScope.addAll(organizeService.selectRolePostSetByRoleIds(customRoleId));
        }
        scope.setDeptScope(deptScope);
        List<SysPostDto> postList = postService.selectListByDeptIds(deptScope);
        postScope.addAll(postList.stream().map(SysPostDto::getId).collect(Collectors.toSet()));
        scope.setPostScope(postScope);
        userScope.addAll(organizeService.selectUserSetByPostIds(postScope));
        scope.setUserScope(userScope);
        if (isCustom > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.CUSTOM.getCode());
            return scope;
        } else if (isDeptAndChild > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.DEPT_AND_CHILD.getCode());
            return scope;
        } else if (isDept > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.DEPT.getCode());
            return scope;
        } else if (isPost > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.POST.getCode());
            return scope;
        } else if (isSelf > 0) {
            scope.setDataScope(AuthorityConstants.DataScope.SELF.getCode());
            return scope;
        }
        scope.setDataScope(AuthorityConstants.DataScope.NONE.getCode());
        return scope;
    }

    /**
     * 登录校验 | 获取路由路径集合
     *
     * @param roleIds  角色Id集合
     * @param userType 用户标识
     * @return 路由路径集合
     */
    @Override
    public Map<String, String> getMenuRouteMap(Set<Long> roleIds, String userType) {
        if (SecurityUtils.isAdminTenant())
            return SysUserDto.isAdmin(userType)
                    ? menuService.getLessorRouteMap()
                    : menuService.getRouteMap(roleIds);
        else
            return SysUserDto.isAdmin(userType)
                    ? menuService.getRouteMap()
                    : menuService.getRouteMap(roleIds);
    }
}
