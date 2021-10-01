package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SystemMenu;
import com.xueyi.system.authority.mapper.SysAuthorityMapper;
import com.xueyi.system.authority.service.ISysAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysAuthorityServiceImpl implements ISysAuthorityService {

    @Autowired
    private SysAuthorityMapper authorityMapper;

    @Autowired
    private ISysAuthorityService authorityService;

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    @Override
    @DataScope(eAlias = "sor")
    public List<SysRole> selectRoleListByTenantId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByTenantId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    @Override
    public List<SysRole> selectRoleListByEnterpriseId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByEnterpriseId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据部门Id查询角色信息集合
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    @Override
    public List<SysRole> selectRoleListByDeptId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByDeptId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据岗位Id查询角色信息集合
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    @Override
    public List<SysRole> selectRoleListByPostId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByPostId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据用户Id查询角色信息集合
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 租户Id
     * @return 角色信息集合
     */
    @Override
    public List<SysRole> selectRoleListByUserId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByUserId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 装配模块-菜单选择 | 半选 | 全选
     *
     * @param roles 角色信息集合
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> assembleSystemMenuSet(List<SysRole> roles) {
        Set<SystemMenu> normalHalfSet, forwardHalfSet, reverseHalfSet,
                normalWholeSet, forwardWholeSet, reverseWholeSet;
        normalHalfSet = new HashSet<>();
        forwardHalfSet = new HashSet<>();
        reverseHalfSet = new HashSet<>();
        normalWholeSet = new HashSet<>();
        forwardWholeSet = new HashSet<>();
        reverseWholeSet = new HashSet<>();
        for (SysRole role : roles) {
            switch (role.getType()) {
                case AuthorityConstants.NORMAL_TYPE:
                    normalHalfSet.addAll(role.getHalfIds());
                    normalWholeSet.addAll(role.getWholeIds());
                    break;
                case AuthorityConstants.DERIVE_TENANT_TYPE:
                    if (!SecurityUtils.isAdminTenant()) {
                        forwardHalfSet.addAll(role.getHalfIds());
                        forwardWholeSet.addAll(role.getWholeIds());
                    }
                    break;
                case AuthorityConstants.DERIVE_ENTERPRISE_TYPE:
                case AuthorityConstants.DERIVE_DEPT_TYPE:
                case AuthorityConstants.DERIVE_POST_TYPE:
                case AuthorityConstants.DERIVE_USER_TYPE:
                    reverseHalfSet.addAll(role.getHalfIds());
                    reverseWholeSet.addAll(role.getWholeIds());
            }
        }
        // 常规和租户衍生 交集
        if (!SecurityUtils.isAdminTenant()) {
            normalHalfSet.retainAll(forwardHalfSet);
            normalWholeSet.retainAll(forwardWholeSet);
        }
        // 常规和企业|部门|岗位|用户衍生 差集
        normalHalfSet.removeAll(reverseHalfSet);
        normalWholeSet.removeAll(reverseWholeSet);
        Map<String, Set<SystemMenu>> map = new HashMap<>();
        map.put("halfIds", normalHalfSet);
        map.put("wholeIds", normalWholeSet);
        return map;
    }

    /**
     * 装配菜单集合
     *
     * @param roles 角色信息集合
     * @return 菜单集合
     */
    @Override
    public Set<SysMenu> selectSystemMenuSet(List<SysRole> roles) {
        Map<String, Set<SystemMenu>> map = assembleSystemMenuSet(roles);
        Set<SystemMenu> halfIds = map.get("halfIds");
        Set<SystemMenu> wholeIds = map.get("wholeIds");
        halfIds.addAll(wholeIds);
        return halfIds.stream().map(halfId -> new SysMenu(halfId.getUid())).collect(Collectors.toSet());
    }
}
