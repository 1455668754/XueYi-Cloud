package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.multiTenancy.SortUtils;
import com.xueyi.common.core.utils.multiTenancy.TreeBuildUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SystemMenu;
import com.xueyi.system.authority.mapper.SysAuthorityMapper;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.utils.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 根据企业Id获取模块-菜单集合 | 租管级
     *
     * @param enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectLessorMenuScope(Long enterpriseId) {
        return buildSystemMenuTree(AuthorityUtils.getSystemMenuCache(enterpriseId));
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 租管级
     *
     * @param enterpriseId 企业Id
     * @param sourceName   指定源
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    @DS("#sourceName")
    public Map<String, Set<SystemMenu>> selectLessorMenuRange(Long enterpriseId, String sourceName) {
        return assembleSystemMenuSet(enterpriseId,authorityService.selectRoleListByTenantId(enterpriseId), EnterpriseUtils.isAdminTenant(enterpriseId), false);
    }

    /**
     * 根据租户Id查询角色信息集合 | 租户级
     *
     * @param enterpriseId 企业Id
     * @return 角色信息集合
     */
    @Override
    public List<SysRole> selectRoleListByTenantId(Long enterpriseId) {
        Set<Long> roleIds = authorityMapper.selectRoleListByTenantId(enterpriseId);
        return AuthorityUtils.getRoleListCache(enterpriseId, roleIds);
    }

    /**
     * 根据企业Id获取模块-菜单集合 | 租户级
     *
     * @param role 角色信息 | enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectTenantMenuScope(SysRole role) {
        return selectSystemMenuTree(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false);
    }


    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 租户级
     *
     * @param role 角色信息 | enterpriseId 企业Id
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> selectTenantMenuRange(SysRole role) {
        return assembleSystemMenuSet(SecurityUtils.getEnterpriseId(),authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false);
    }

    /**
     * 根据租户Id查询角色信息集合 | 企业级
     *
     * @param role 角色信息 | enterpriseId 企业Id
     * @return 角色信息集合
     */
    @Override
    @DataScope(eAlias = "sor")
    public List<SysRole> selectRoleListByEnterpriseId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByEnterpriseId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据企业Id获取模块-菜单集合 | 企业级
     *
     * @param role 角色信息 | enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectEnterpriseMenuScope(SysRole role) {
        return selectSystemMenuTree(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByEnterpriseId(role), SecurityUtils.isAdminTenant(), false);
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 企业级
     *
     * @param role 角色信息 | enterpriseId 企业Id
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> selectEnterpriseMenuRange(SysRole role) {
        return assembleSystemMenuSet(SecurityUtils.getEnterpriseId(),authorityService.selectRoleListByEnterpriseId(role), SecurityUtils.isAdminTenant(), false);
    }

    /**
     * 根据部门Id查询角色信息集合 | 部门级
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 企业Id
     * @return 角色信息集合
     */
    @Override
    @DataScope(eAlias = "sor")
    public List<SysRole> selectRoleListByDeptId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByDeptId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据部门Id获取模块-菜单集合 | 部门级
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectDeptMenuScope(SysRole role) {
        return selectSystemMenuTree(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByDeptId(role), SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据部门Id获取模块-菜单选择 | 半选 | 全选 | 部门级
     *
     * @param role 角色信息 | params.deptId 部门Id | enterpriseId 企业Id
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> selectDeptMenuRange(SysRole role) {
        return assembleSystemMenuSet(SecurityUtils.getEnterpriseId(),authorityService.selectRoleListByDeptId(role), SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据岗位Id查询角色信息集合 | 岗位级
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 企业Id
     * @return 角色信息集合
     */
    @Override
    @DataScope(eAlias = "sor")
    public List<SysRole> selectRoleListByPostId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByPostId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据岗位Id获取模块-菜单集合 | 岗位级
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectPostMenuScope(SysRole role) {
        return selectSystemMenuTree(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByPostId(role), SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据岗位Id获取模块-菜单选择 | 半选 | 全选 | 岗位级
     *
     * @param role 角色信息 | params.postId 岗位Id | enterpriseId 企业Id
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> selectPostMenuRange(SysRole role) {
        return assembleSystemMenuSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByPostId(role), SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据用户Id查询角色信息集合
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 企业Id
     * @return 角色信息集合
     */
    @Override
    @DataScope(eAlias = "sor")
    public List<SysRole> selectRoleListByUserId(SysRole role) {
        Set<Long> roleIds = authorityMapper.selectRoleListByUserId(role);
        return AuthorityUtils.getRoleListCache(SecurityUtils.getEnterpriseId(), roleIds);
    }

    /**
     * 根据用户Id获取模块-菜单集合 | 用户级
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 企业Id
     * @return 模块-菜单集合
     */
    @Override
    public List<TreeSelect> selectUserMenuScope(SysRole role) {
        return selectSystemMenuTree(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByUserId(role), SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据用户Id获取模块-菜单选择 | 半选 | 全选 | 用户级
     *
     * @param role 角色信息 | params.userId 用户Id | enterpriseId 企业Id
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> selectUserMenuRange(SysRole role) {
        return assembleSystemMenuSet(SecurityUtils.getEnterpriseId(),authorityService.selectRoleListByUserId(role),  SecurityUtils.isAdminTenant(), true);
    }

    /**
     * 根据角色信息更新模块-菜单集合
     *
     * @param role       角色信息 | roleId 角色Id | enterpriseId 租户Id | dataScope 数据范围 | params.wholeIds 全选 | params.halfIds 半选
     * @param sourceName 指定源
     */
    @Override
    @Transactional
    @DS("#sourceName")
    public void updateMenuScopeToSourceName(SysRole role, String sourceName) {
        refreshMenuScope(role);
    }

    /**
     * 根据角色信息更新模块-菜单集合
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id | dataScope 数据范围 | params.wholeIds 全选 | params.halfIds 半选
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public void updateMenuScope(SysRole role) {
        refreshMenuScope(role);
    }

    /**
     * 根据角色信息更新模块-菜单集合
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id | dataScope 数据范围 | params.wholeIds 全选 | params.halfIds 半选
     */
    private void refreshMenuScope(SysRole role) {
        authorityMapper.deleteMenuScope(role);
        if (role.getParams().containsKey("wholeIds")) {
            authorityMapper.insertWholeIdsMenuScope(role);
        }
        if (role.getParams().containsKey("halfIds")) {
            authorityMapper.insertHalfIdsMenuScope(role);
        }
    }

    /**
     * 装配模块-菜单选择 | 半选 | 全选
     *
     * @param enterpriseId  企业Id
     * @param roles         角色信息集合
     * @param isAdminTenant 是否租管租户
     * @param hasNormal     有无普通角色权限
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @Override
    public Map<String, Set<SystemMenu>> assembleSystemMenuSet(Long enterpriseId, List<SysRole> roles, boolean isAdminTenant, boolean hasNormal) {
        Set<SystemMenu> allHalfSet, normalHalfSet, forwardHalfSet, reverseHalfSet,
                allWholeSet, normalWholeSet, forwardWholeSet, reverseWholeSet;
        allHalfSet = AuthorityUtils.getSystemMenuCache(enterpriseId);
        allWholeSet = AuthorityUtils.getSystemMenuCache(enterpriseId);
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
                    if (!isAdminTenant) {
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
        //存在普通角色权限更替为普通角色
        if (hasNormal) {
            allHalfSet.retainAll(normalHalfSet);
            allWholeSet.retainAll(normalWholeSet);
        }
        // 常规和租户衍生 交集
        if (!isAdminTenant) {
            allHalfSet.retainAll(forwardHalfSet);
            allWholeSet.retainAll(forwardWholeSet);
        }
        // 常规和企业|部门|岗位|用户衍生 差集
        allHalfSet.removeAll(reverseHalfSet);
        allWholeSet.removeAll(reverseWholeSet);
        Map<String, Set<SystemMenu>> map = new HashMap<>();
        map.put("halfIds", allHalfSet);
        map.put("wholeIds", allWholeSet);
        return map;
    }

    /**
     * 装配菜单集合
     *
     * @param roles         角色信息集合
     * @param isAdminTenant 是否租管租户
     * @return 菜单集合
     */
    @Override
    public Set<SysMenu> selectMenuSet(List<SysRole> roles, boolean isAdminTenant) {
        Set<SystemMenu> systemMenuSet = new HashSet<>();
        Map<String, Set<SystemMenu>> map = assembleSystemMenuSet(SecurityUtils.getEnterpriseId(),roles,  SecurityUtils.isAdminTenant(), true);
        systemMenuSet.addAll(map.get("halfIds"));
        systemMenuSet.addAll(map.get("wholeIds"));
        return systemMenuSet.stream().map(item -> new SysMenu(item.getUid())).collect(Collectors.toSet());
    }

    /**
     * 模块-菜单树装配 | 企业级 | 部门级 | 岗位级 | 用户级
     *
     * @param enterpriseId  企业Id
     * @param roles         角色信息集合
     * @param isAdminTenant 是否租管租户
     * @param hasNormal     有无普通角色权限
     * @return 模块-菜单树
     */
    private List<TreeSelect> selectSystemMenuTree(Long enterpriseId, List<SysRole> roles, boolean isAdminTenant, boolean hasNormal) {
        Set<SystemMenu> systemMenuSet = new HashSet<>();
        Map<String, Set<SystemMenu>> map = assembleSystemMenuSet(enterpriseId,roles,  isAdminTenant, hasNormal);
        systemMenuSet.addAll(map.get("halfIds"));
        systemMenuSet.addAll(map.get("wholeIds"));
        return buildSystemMenuTree(systemMenuSet);
    }

    /**
     * 模块-菜单树装配
     *
     * @param systemMenuSet 模块-菜单集合
     * @return 模块-菜单树
     */
    public List<TreeSelect> buildSystemMenuTree(Set<SystemMenu> systemMenuSet) {
        List<SystemMenu> systemMenus = TreeBuildUtils.buildSystemMenuTree(SortUtils.sortSetToList(systemMenuSet), "Uid", "FUid", "children", MenuConstants.SYSTEM_TOP_NODE);
        return systemMenus.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}