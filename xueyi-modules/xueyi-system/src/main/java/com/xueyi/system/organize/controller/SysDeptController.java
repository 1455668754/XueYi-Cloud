package com.xueyi.system.organize.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.SubTreeController;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 部门管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends SubTreeController<SysDeptDto, ISysDeptService, SysPostDto, ISysPostService> {

    @Autowired
    private ISysOrganizeService organizeService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "部门";
    }

    /** 定义子数据名称 */
    @Override
    protected String getSubName() {
        return "岗位";
    }

    /**
     * 新增部门 | 内部调用
     */
    @InnerAuth
    @PostMapping("/inner/add")
    public R<SysDeptDto> addInner(@RequestBody SysDeptDto dept) {
        return baseService.addInner(dept) > 0 ? R.ok(dept) : R.fail();
    }

    /**
     * 查询部门列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_DEPT_LIST)
    public AjaxResult list(SysDeptDto dept) {
        return super.list(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @GetMapping("/list/exclude")
    @RequiresPermissions(Auth.SYS_DEPT_LIST)
    public AjaxResult listExNodes(SysDeptDto dept) {
        return super.listExNodes(dept);
    }

    /**
     * 查询部门详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_DEPT_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @GetMapping(value = "/auth/{id}")
    @RequiresPermissions(Auth.SYS_DEPT_AUTH)
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        return AjaxResult.success(organizeService.selectDeptRoleMerge(id));
    }
    
    /**
     * 部门导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_DEPT_EXPORT)
    @Log(title = "部门管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysDeptDto dept) {
        super.export(response, dept);
    }

    /**
     * 部门新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_DEPT_ADD)
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDeptDto dept) {
        return super.add(dept);
    }

    /**
     * 部门修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_DEPT_EDIT)
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDeptDto dept) {
        return super.edit(dept);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @PutMapping(value = "/auth")
    @RequiresPermissions(Auth.SYS_DEPT_AUTH)
    public AjaxResult editRoleAuth(@RequestBody SysDeptDto dept) {
        organizeService.editDeptRoleMerge(dept.getId(), dept.getRoleIds());
        return AjaxResult.success();
    }

    /**
     * 部门修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_DEPT_EDIT, Auth.SYS_DEPT_ES}, logical = Logical.OR)
    @Log(title = "部门管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDeptDto dept) {
        return super.editStatus(dept);
    }

    /**
     * 部门批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_DEPT_DELETE)
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取部门选择框列表
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
    protected void AEHandleValidated(BaseConstants.Operate operate, SysDeptDto dept) {
        if (baseService.checkDeptCodeUnique(dept.getId(), dept.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，部门编码已存在", operate.getInfo(), getNodeName(), dept.getName()));
        else if (baseService.checkNameUnique(dept.getId(), dept.getParentId(), dept.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，部门名称已存在", operate.getInfo(), getNodeName(), dept.getName()));
    }
}
