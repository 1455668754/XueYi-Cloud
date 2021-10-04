package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysRoleSystemMenuService roleSystemMenuService;

    /**
     * 查询模块-菜单信息列表
     */
    @PreAuthorize(hasPermi = "system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        return AjaxResult.success(menuService.mainSelectSystemMenuList(menu));
    }

    /**
     * 根据菜单Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysMenu menu) {
        return AjaxResult.success(menuService.mainSelectMenuById(menu));
    }

    /**
     * 新增菜单
     */
    @PreAuthorize(hasPermi = "system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.mainCheckMenuNameUnique(menu)) {
            return AjaxResult.error("新增菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (MenuConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("新增菜单'" + menu.getName() + "'失败，地址必须以http(s)://开头");
        } else if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, menu.getIsCommon()) && !SecurityUtils.isAdminTenant()) {
            return AjaxResult.error("新增菜单'" + menu.getName() + "'失败，仅租管账户可新增公共菜单");
        }
        return toAjax(menuService.mainInsertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize(hasPermi = "system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.mainCheckMenuNameUnique(menu)) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (MenuConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，上级菜单不能选择自己");
        } else if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, menu.getIsCommon()) && !SecurityUtils.isAdminTenant()) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，仅租管账户可修改公共菜单");
        }
        return toAjax(menuService.mainUpdateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize(hasPermi = "system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysMenu menu) {
        if (menuService.mainHasChildByMenuId(menu)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.mainCheckMenuExistRole(menu)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.mainDeleteMenuById(menu));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(SysMenu menu) {
        List<SysMenu> menus = menuService.getRoutes(menu);
        return AjaxResult.success(menuService.buildMenus(menus));
    }





















    /**
     * 获取指定企业账号的租管衍生角色菜单范围信息 | 租管系统使用方法
     */
    @GetMapping("/getMenuScope/administrator")
    public AjaxResult getMenuScope(SysRoleSystemMenu systemMenu) {
        systemMenu.setSourceName(EnterpriseUtils.getMainSourceName(systemMenu.getEnterpriseId()));
        return AjaxResult.success(roleSystemMenuService.getEnterpriseMenuScopeById(systemMenu));
    }

    /**
     * 修改保存指定企业账号的租管衍生角色菜单权限 | 租管系统使用方法
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/authMenuScope/administrator")
    public AjaxResult authMenuScope(@RequestBody SysRoleSystemMenu systemMenu) {
        systemMenu.setSourceName(EnterpriseUtils.getMainSourceName(systemMenu.getEnterpriseId()));
        return toAjax(roleSystemMenuService.authMenuScopeById(systemMenu));
    }
}