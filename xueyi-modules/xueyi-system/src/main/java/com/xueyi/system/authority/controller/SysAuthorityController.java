package com.xueyi.system.authority.controller;

import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.authority.service.ISysAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 根据租户Id获取模块-菜单范围 | 租户级
     */
    @GetMapping(value = "/tenantScope/{enterpriseId}")
    public AjaxResult getTenantMenuScope(@PathVariable Long enterpriseId) {
        return AjaxResult.success(authorityService.selectTenantMenuScope(enterpriseId));
    }

    /**
     * 根据租户Id获取模块-菜单选择 | 半选 | 全选 | 租户级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/tenantRange/{enterpriseId}")
    public AjaxResult getTenantMenuRange(@PathVariable Long enterpriseId) {
        return AjaxResult.success(authorityService.selectTenantMenuRange(enterpriseId));
    }

    /**
     * 根据企业Id获取模块-菜单范围 | 企业级
     */
    @GetMapping(value = "/enterpriseScope")
    public AjaxResult getEnterpriseMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectEnterpriseMenuScope(role));
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
     * 根据企业Id获取模块-菜单范围 | 部门级
     */
    @GetMapping(value = "/deptScope")
    public AjaxResult getDeptMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectDeptMenuScope(role));
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 部门级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/deptRange")
    public AjaxResult getDeptMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectDeptMenuRange(role));
    }

    /**
     * 根据企业Id获取模块-菜单范围 | 岗位级
     */
    @GetMapping(value = "/postScope")
    public AjaxResult getPostMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectPostMenuScope(role));
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 岗位级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/postRange")
    public AjaxResult getPostMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectPostMenuRange(role));
    }

    /**
     * 根据企业Id获取模块-菜单范围 | 用户级
     */
    @GetMapping(value = "/userScope")
    public AjaxResult getUserMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectUserMenuScope(role));
    }

    /**
     * 根据企业Id获取模块-菜单选择 | 半选 | 全选 | 用户级
     *
     * @return map集合 | halfIds 半选模块-菜单 | wholeIds 全选模块-菜单
     */
    @GetMapping(value = "/userRange")
    public AjaxResult getUserMenuRange(SysRole role) {
        return AjaxResult.success(authorityService.selectUserMenuRange(role));
    }
}
