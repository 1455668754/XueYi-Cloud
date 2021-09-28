package com.xueyi.system.authority.controller;

import java.util.List;

import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import com.xueyi.system.source.service.IDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.authority.service.ISysMenuService;

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
    private IDataSourceService dataSourceService;

    @Autowired
    private ISysRoleSystemMenuService roleSystemMenuService;

    /**
     * 获取指定企业账号的租管衍生角色菜单范围信息 | 租管系统使用方法
     */
    @GetMapping("/getMenuScope/administrator")
    public AjaxResult getMenuScope(SysRoleSystemMenu systemMenu) {
        return AjaxResult.success(roleSystemMenuService.getEnterpriseMenuScopeById(getSourceMaster(systemMenu)));
    }

    /**
     * 修改保存指定企业账号的租管衍生角色菜单权限 | 租管系统使用方法
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/authMenuScope/administrator")
    public AjaxResult authMenuScope(@RequestBody SysRoleSystemMenu systemMenu) {
        return toAjax(roleSystemMenuService.authMenuScopeById(getSourceMaster(systemMenu)));
    }

    /**
     * 根据菜单Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysMenu menu) {
        return AjaxResult.success(menuService.selectMenuById(menu));
    }

    /**
     * 新增菜单
     */
    @PreAuthorize(hasPermi = "system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }else if (MenuConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())){
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize(hasPermi = "system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }else if (MenuConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())){
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize(hasPermi = "system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysMenu menu) {
        if (menuService.hasChildByMenuId(menu)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menu)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menu));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(menu);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取企业的主数据源
     */
    private SysRoleSystemMenu getSourceMaster(SysRoleSystemMenu systemMenu){
        Source source = dataSourceService.getSourceByEnterpriseId(systemMenu.getEnterpriseId());
        systemMenu.setSourceName(source.getMaster());
        return systemMenu;
    }
}