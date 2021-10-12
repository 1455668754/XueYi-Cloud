package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.cache.service.ISysCacheInitService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/authority")
public class SysAuthorityController extends BaseController {

    @Autowired
    private ISysAuthorityService authorityService;

    @Autowired
    private ISysCacheInitService cacheInitService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取当前租户模块-菜单范围 | 租户级
     */
    @GetMapping(value = "/lessorScope")
    public AjaxResult getLessorMenuScope() {
        return AjaxResult.success(authorityService.selectLessorMenuScope(SecurityUtils.getEnterpriseId()));
    }

    /**
     * 根据租户Id获取模块-菜单范围 | 租户级
     */
    @GetMapping(value = "/lessorScope/{enterpriseId}")
    public AjaxResult getLessorMenuScope(@PathVariable Long enterpriseId) {
        return AjaxResult.success(authorityService.selectLessorMenuScope(enterpriseId));
    }

    /**
     * 根据租户Id获取模块-菜单选择 | 半选 | 全选 | 租户级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/lessorRange/{enterpriseId}")
    public AjaxResult getLessorMenuRange(@PathVariable Long enterpriseId) {
        return AjaxResult.success(authorityService.selectLessorMenuRange(enterpriseId, EnterpriseUtils.getMainSourceName(enterpriseId)));
    }

    /**
     * 根据租户Id获取模块-菜单范围 | 租户级
     */
    @GetMapping(value = "/tenantScope")
    public AjaxResult getTenantMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectTenantMenuScope(role));
    }

    /**
     * 根据租户Id更新模块-菜单范围 | 租户级
     */
    @PreAuthorize(hasPermi = "tenant:tenant:role")
    @Log(title = "租户菜单配置", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/tenantScopeSet")
    public AjaxResult setTenantMenuScope(@RequestBody SysRole role) {
        if (EnterpriseUtils.isAdminTenant(role.getEnterpriseId())) {
            return AjaxResult.error("租管租户禁止进行菜单配置");
        }
        String sourceName = EnterpriseUtils.getMainSourceName(role.getEnterpriseId());
        SysRole checkRole = roleService.selectRoleIdByDeriveIdToSourceName(new SysRole(AuthorityConstants.DERIVE_TENANT_TYPE, role.getEnterpriseId(), role.getEnterpriseId()), sourceName);
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScopeToSourceName(role, sourceName);
        cacheInitService.refreshRoleCacheByRoleIdToSourceName(role, sourceName);
        return toAjax(1);
    }

    /**
     * 根据租户Id获取模块-菜单选择 | 半选 | 全选 | 租户级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/tenantRange")
    public AjaxResult getTenantMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectTenantMenuRange(role));
    }

    /**
     * 根据企业Id获取模块-菜单范围 | 企业级
     */
    @GetMapping(value = "/enterpriseScope")
    public AjaxResult getEnterpriseMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectEnterpriseMenuScope(role));
    }

    /**
     * 根据企业Id更新模块-菜单范围 | 企业级
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/enterpriseScopeSet")
    public AjaxResult setEnterpriseMenuScope(@RequestBody SysRole role) {
        SysRole checkRole = roleService.selectRoleIdByDeriveId(new SysRole(AuthorityConstants.DERIVE_ENTERPRISE_TYPE, SecurityUtils.getEnterpriseId(), SecurityUtils.getEnterpriseId()));
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScope(role);
        cacheInitService.refreshRoleCacheByRoleIdToIsolate(role);
        return toAjax(1);
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 企业级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/enterpriseRange")
    public AjaxResult getEnterpriseMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectEnterpriseMenuRange(role));
    }

    /**
     * 根据部门Id获取模块-菜单范围 | 部门级
     */
    @GetMapping(value = "/deptScope")
    public AjaxResult getDeptMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectDeptMenuScope(role));
    }

    /**
     * 根据部门Id更新模块-菜单范围 | 部门级
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/deptScopeSet")
    public AjaxResult setDeptMenuScope(@RequestBody SysRole role) {
        SysRole checkRole = roleService.selectRoleIdByDeriveId(new SysRole(AuthorityConstants.DERIVE_DEPT_TYPE, (Long) role.getParams().get("deptId"), SecurityUtils.getEnterpriseId()));
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScope(role);
        cacheInitService.refreshRoleCacheByRoleIdToIsolate(role);
        return toAjax(1);
    }

    /**
     * 根据部门Id获取模块-菜单选择 | 半选 | 全选 | 部门级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/deptRange")
    public AjaxResult getDeptMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectDeptMenuRange(role));
    }

    /**
     * 根据岗位Id获取模块-菜单范围 | 岗位级
     */
    @GetMapping(value = "/postScope")
    public AjaxResult getPostMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectPostMenuScope(role));
    }

    /**
     * 根据岗位Id更新模块-菜单范围 | 岗位级
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/postScopeSet")
    public AjaxResult setPostMenuScope(@RequestBody SysRole role) {
        SysRole checkRole = roleService.selectRoleIdByDeriveId(new SysRole(AuthorityConstants.DERIVE_POST_TYPE, (Long) role.getParams().get("postId"), SecurityUtils.getEnterpriseId()));
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScope(role);
        cacheInitService.refreshRoleCacheByRoleIdToIsolate(role);
        return toAjax(1);
    }

    /**
     * 根据岗位Id获取模块-菜单选择 | 半选 | 全选 | 岗位级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/postRange")
    public AjaxResult getPostMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectPostMenuRange(role));
    }

    /**
     * 根据用户Id获取模块-菜单范围 | 用户级
     */
    @GetMapping(value = "/userScope")
    public AjaxResult getUserMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectUserMenuScope(role));
    }

    /**
     * 根据用户Id更新模块-菜单范围 | 用户级
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/userScopeSet")
    public AjaxResult setUserMenuScope(@RequestBody SysRole role) {
        SysRole checkRole = roleService.selectRoleIdByDeriveId(new SysRole(AuthorityConstants.DERIVE_USER_TYPE, (Long) role.getParams().get("userId"), SecurityUtils.getEnterpriseId()));
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScope(role);
        cacheInitService.refreshRoleCacheByRoleIdToIsolate(role);
        return toAjax(1);
    }

    /**
     * 根据用户Id获取模块-菜单选择 | 半选 | 全选 | 用户级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/userRange")
    public AjaxResult getUserMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectUserMenuRange(role));
    }

    /**
     * 根据角色Id更新模块-菜单范围 | 角色级
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/roleScopeSet")
    public AjaxResult setRoleMenuScope(@RequestBody SysRole role) {
        SysRole checkRole = roleService.selectRoleById(new SysRole(role.getRoleId()));
        if (!StringUtils.equals(AuthorityConstants.NORMAL_TYPE, checkRole.getType())) {
            return AjaxResult.error("衍生角色禁止修改");
        }
        role.setRoleId(checkRole.getRoleId());
        role.setDataScope(checkRole.getDataScope());
        authorityService.updateMenuScope(role);
        cacheInitService.refreshRoleCacheByRoleIdToIsolate(role);
        return toAjax(1);
    }

    /**
     * 根据角色Id获取模块-菜单选择 | 半选 | 全选 | 角色级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/roleRange")
    public AjaxResult getRoleMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectRoleMenuRange(role));
    }

    /**
     * 根据角色Id获取部门-岗位选择 | 角色级
     *
     * @return map集合 | deptPostIds 部门-岗位
     */
    @GetMapping(value = "/roleDataRange")
    public AjaxResult getRoleDataRange(SysRole role) {
        return AjaxResult.success(authorityService.selectRoleDataRange(role));
    }
}