package com.xueyi.system.organize.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.xueyi.system.api.organize.SysUser;
import org.apache.commons.lang3.ArrayUtils;
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
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.organize.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize(hasPermi = "system:dept:list")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        return AjaxResult.success(deptService.selectDeptList(dept));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize(hasPermi = "system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().longValue() == deptId.longValue()
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize(hasPermi = "system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptCodeUnique(dept.getDeptId(), dept.getDeptCode()))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept.getDeptId(), dept.getParentId(), dept.getDeptName()))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize(hasPermi = "system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptCodeUnique(dept.getDeptId(), dept.getDeptCode()))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept.getDeptId(), dept.getParentId(), dept.getDeptName()))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId()) || UserConstants.NOT_UNIQUE.equals(deptService.checkIsChild(dept.getDeptId(), dept.getParentId()))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己或自己的子部门");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.checkNormalChildrenCount(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 修改部门-角色关系
     */
    @PreAuthorize(hasPermi = "system:role:set")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeDeptRole")
    public AjaxResult editDeptRole(@Validated @RequestBody SysDept dept) {
        return toAjax(deptService.updateDeptRole(dept.getDeptId(), dept.getRoleIds()));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysDept dept) {
        if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.checkNormalChildrenCount(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        } else if (StringUtils.equals(UserConstants.DEPT_NORMAL, dept.getStatus())
                && UserConstants.DEPT_DISABLE.equals(deptService.checkDeptStatus(dept.getParentId()))) {
            return AjaxResult.error("启用失败，该部门的父级部门已被禁用！");
        }
        return toAjax(deptService.updateDeptStatus(dept.getDeptId(), dept.getStatus()));
    }

    /**
     * 删除部门
     */
    @PreAuthorize(hasPermi = "system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistPost(deptId)) {
            return AjaxResult.error("部门存在岗位,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }
}