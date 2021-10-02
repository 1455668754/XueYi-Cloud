package com.xueyi.system.authority.controller;

import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.security.annotation.PreAuthorize;
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
     * 根据租户Id获取菜单范围 | 租户级菜单
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/tenantScope/{enterpriseId}")
    public AjaxResult getTenantMenuScope(@PathVariable Long enterpriseId) {
        return AjaxResult.success(authorityService.selectTenantMenuScope(enterpriseId));
    }

    /**
     * 根据企业Id获取菜单范围 | 企业级菜单
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/enterpriseScope")
    public AjaxResult getEnterpriseMenuScope(SysRole role) {
        return AjaxResult.success(authorityService.selectEnterpriseMenuScope(role));
    }
}
