package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.authority.service.ISysSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 子系统模块Controller
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/system")
public class SysSystemController extends BaseController {
    @Autowired
    private ISysSystemService systemService;

    /**
     * 查询首页可展示子系统模块列表
     */
    @GetMapping("/viewList")
    public TableDataInfo viewList() {
        List<SysSystem> list = systemService.homePageView();
        return getDataTable(list);
    }

    /**
     * 查询子系统模块列表
     */
    @PreAuthorize(hasPermi = "system:system:list")
    @GetMapping("/list")
    public TableDataInfo list(SysSystem sysSystem) {
        startPage();
        List<SysSystem> list = systemService.selectSystemList(sysSystem);
        return getDataTable(list);
    }

    /**
     * 加载对应模块&菜单列表树 | searchValue === PERMIT_ALL 获取所有权限内模块&菜单 | 无衍生角色
     */
    @GetMapping(value = "/systemMenuTreePermitAll")
    public AjaxResult getListPermitAll(SysSystem sysSystem) {
        sysSystem.setSearchValue(MenuConstants.PERMIT_ALL);
        return AjaxResult.success(systemService.buildSystemMenuTreeSelect(sysSystem));
    }

    /**
     * 加载对应模块&菜单列表树 | searchValue === PERMIT_ADMINISTRATOR 仅获取超管权限内模块&菜单 | 衍生角色仅获取超管衍生
     */
    @GetMapping(value = "/systemMenuTreePermitAdministrator")
    public AjaxResult getListPermitAdministrator(SysSystem sysSystem) {
        sysSystem.setSearchValue(MenuConstants.PERMIT_ADMINISTRATOR);
        return AjaxResult.success(systemService.buildSystemMenuTreeSelect(sysSystem));
    }

    /**
     * 加载对应模块&菜单列表树 | searchValue === PERMIT_ENTERPRISE 仅获取租户权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     */
    @GetMapping(value = "/systemMenuTreePermitEnterprise")
    public AjaxResult getListPermitEnterprise(SysSystem sysSystem) {
        sysSystem.setSearchValue(MenuConstants.PERMIT_ENTERPRISE);
        return AjaxResult.success(systemService.buildSystemMenuTreeSelect(sysSystem));
    }

    /**
     * 加载对应模块&菜单列表树 | searchValue === PERMIT_PERSONAL_SCREEN_DERIVE 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     */
    @GetMapping(value = "/systemMenuTreePermitPersonalScreenDerice")
    public AjaxResult getListPermitPersonalScreenDerice(SysSystem sysSystem) {
        sysSystem.setSearchValue(MenuConstants.PERMIT_PERSONAL_SCREEN_DERIVE);
        return AjaxResult.success(systemService.buildSystemMenuTreeSelect(sysSystem));
    }
    
    /**
     * 加载对应角色模块&菜单列表树 | searchValue === PERMIT_PERSONAL 仅获取个人权限内模块&菜单 | 衍生角色获取自身组织衍生&超管衍生&租户衍生
     */
    @GetMapping(value = "/systemMenuTreePermitPersonal")
    public AjaxResult getListPermitPersonal(SysSystem sysSystem) {
        sysSystem.setSearchValue(MenuConstants.PERMIT_PERSONAL);
        return AjaxResult.success(systemService.buildSystemMenuTreeSelect(sysSystem));
    }

    /**
     * 获取子系统模块详细信息
     */
    @PreAuthorize(hasPermi = "system:system:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysSystem sysSystem) {
        return AjaxResult.success(systemService.selectSystemById(sysSystem));
    }

    /**
     * 新增子系统模块
     */
    @PreAuthorize(hasPermi = "system:system:add")
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSystem sysSystem) {
        return toAjax(systemService.insertSystem(sysSystem));
    }

    /**
     * 修改子系统模块
     */
    @PreAuthorize(hasPermi = "system:system:edit")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSystem sysSystem) {
        return toAjax(systemService.updateSystem(sysSystem));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:system:edit")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysSystem sysSystem) {
        return toAjax(systemService.updateSystemStatus(sysSystem));
    }

    /**
     * 删除子系统模块
     */
    @PreAuthorize(hasPermi = "system:system:remove")
    @Log(title = "模块管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysSystem sysSystem) {
        return toAjax(systemService.deleteSystemByIds(sysSystem));
    }
}