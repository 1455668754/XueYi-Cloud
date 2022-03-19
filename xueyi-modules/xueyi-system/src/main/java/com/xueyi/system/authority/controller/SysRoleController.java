package com.xueyi.system.authority.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.service.ISysAuthService;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 角色管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController<SysRoleDto, ISysRoleService> {

    @Autowired
    private ISysAuthService authService;

    @Autowired
    private ISysOrganizeService organizeService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "角色";
    }

    /**
     * 查询角色列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_ROLE_LIST)
    public AjaxResult list(SysRoleDto role) {
        return super.list(role);
    }

    /**
     * 查询角色详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_ROLE_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 获取角色功能权限 | 叶子节点
     */
    @GetMapping("/auth/{id}")
    @RequiresPermissions(Auth.SYS_ROLE_AUTH)
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        List<SysAuthTree> leafNodes = TreeUtils.getLeafNodes(TreeUtils.buildTree(authService.selectRoleAuth(id)));
        return AjaxResult.success(leafNodes.stream().map(SysAuthTree::getId).toArray(Long[]::new));
    }

    /**
     * 获取角色组织权限
     */
    @GetMapping("/organize/{id}")
    @RequiresPermissions(Auth.SYS_ROLE_AUTH)
    public AjaxResult getRoleOrganize(@PathVariable Long id) {
        return AjaxResult.success(organizeService.selectRoleOrganizeMerge(id));
    }

    /**
     * 角色导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_ROLE_EXPORT)
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysRoleDto role) {
        super.export(response, role);
    }

    /**
     * 角色新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_ROLE_ADD)
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysRoleDto role) {
        return super.add(role);
    }

    /**
     * 角色修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_ROLE_EDIT)
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysRoleDto role) {
        return super.edit(role);
    }

    /**
     * 角色修改功能权限
     */
    @PutMapping("/auth")
    @RequiresPermissions(Auth.SYS_ROLE_AUTH)
    @Log(title = "角色管理", businessType = BusinessType.AUTH)
    public AjaxResult editAuth(@RequestBody SysRoleDto role) {
        authService.editRoleAuth(role.getId(), role.getAuthIds());
        return AjaxResult.success();
    }

    /**
     * 角色修改组织权限
     */
    @PutMapping("/organize")
    @RequiresPermissions(Auth.SYS_ROLE_AUTH)
    @Log(title = "角色管理", businessType = BusinessType.AUTH)
    public AjaxResult editOrganize(@RequestBody SysRoleDto role) {
        return AjaxResult.success(baseService.updateDataScope(role));
    }

    /**
     * 角色修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_ROLE_EDIT, Auth.SYS_ROLE_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "角色管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysRoleDto role) {
        return super.editStatus(role);
    }

    /**
     * 角色批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_ROLE_DELETE)
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取角色选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandleValidated(BaseConstants.Operate operate, SysRoleDto role) {
        if (baseService.checkRoleCodeUnique(role.getId(), role.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色编码已存在", operate.getInfo(), getNodeName(), role.getName()));
        else if (baseService.checkNameUnique(role.getId(), role.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色名称已存在", operate.getInfo(), getNodeName(), role.getName()));
        else if (baseService.checkRoleKeyUnique(role.getId(), role.getRoleKey()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色权限已存在", operate.getInfo(), getNodeName(), role.getName()));
        // 修改禁止操作权限范围
        if (operate.isEdit())
            role.setDataScope(null);
    }
}
