package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.cache.service.ISysCacheInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private ISysCacheInitService cacheInitService;

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
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysRole role) {
        return AjaxResult.success(roleService.selectRoleById(role));
    }

    /**
     * 新增角色
     */
    @PreAuthorize(hasPermi = "system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleCodeUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getName() + "'失败，角色编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getName() + "'失败，角色权限已存在");
        }
        return toAjax(refreshRoleCache(roleService.insertRole(role),role));
    }

    /**
     * 修改保存角色
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleCodeUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getName() + "'失败，角色编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getName() + "'失败，角色权限已存在");
        }
        return toAjax(refreshRoleCache(roleService.updateRole(role),role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        return toAjax(refreshRoleCache(roleService.updateRoleStatus(role),role));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope/save")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        return toAjax(refreshRoleCache(roleService.authDataScope(role),role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize(hasPermi = "system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysRole role) {
        Set<SysRole> before = roleService.checkRoleListByIds(role);
        int rows = roleService.deleteRoleByIds(role);
        if(rows>0){
            Set<SysRole> after = roleService.checkRoleListByIds(role);
            before.removeAll(after);
            for (SysRole delRole: before) {
                AuthorityUtils.deleteRoleCache(SecurityUtils.getEnterpriseId(),delRole.getRoleId());
            }
        }
        return toAjax(rows);
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
    public AjaxResult optionSelect() {
        return AjaxResult.success(roleService.selectRoleAll());
    }

    /**
     * 通过角色Id更新角色缓存
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 企业Id
     * @return 结果
     */
    private int refreshRoleCache(int rows,SysRole role){
        if(rows>0){
            cacheInitService.refreshRoleCacheByRoleId(role);
        }
        return rows;
    }
}
