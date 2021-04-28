package com.xueyi.system.authority.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.authority.service.ISysRoleService;

/**
 * 角色信息
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取角色列表
     */
    @PreAuthorize(hasPermi = "system:role:list")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 根据角色Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 根据角色Id获取菜单范围信息
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping(value = "/menuScope/{roleId}")
    public AjaxResult getMenuScope(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectMenuScopeById(roleId));
    }

    /**
     * 根据角色Id获取数据范围信息
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping(value = "/dataScope/{roleId}")
    public AjaxResult getDataScope(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectDataScopeById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize(hasPermi = "system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleCodeUnique(role.getRoleId(), role.getRoleCode()))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role.getRoleId(), role.getRoleName()))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role.getRoleId(), role.getRoleKey()))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleCodeUnique(role.getRoleId(), role.getRoleCode()))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role.getRoleId(), role.getRoleName()))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role.getRoleId(), role.getRoleKey()))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存菜单权限
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/menuScope")
    public AjaxResult menuScope(@RequestBody SysRole role) {
        return toAjax(roleService.authMenuScope(role));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        return toAjax(roleService.updateRoleStatus(role.getRoleId(), role.getStatus()));
    }

    /**
     * 删除角色
     */
    @PreAuthorize(hasPermi = "system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }


    /**
     * 导出角色列表
     */
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:role:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) throws IOException {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect()
    {
        return AjaxResult.success(roleService.selectRoleAll());
    }
}
