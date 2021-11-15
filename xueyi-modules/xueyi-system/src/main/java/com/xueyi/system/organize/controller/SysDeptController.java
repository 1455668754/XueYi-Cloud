package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.TreeBuildUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.organize.service.ISysDeptService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

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
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        return AjaxResult.success(TreeBuildUtils.buildSystemMenuTree(deptService.selectDeptList(dept), "deptId", "parentId", "children", null, false));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list/exclude")
    public AjaxResult excludeChild(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().longValue() == dept.getDeptId().longValue()
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), dept.getDeptId() + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(TreeBuildUtils.buildSystemMenuTree(depts, "deptId", "parentId", "children", null, false));
    }

    /**
     * 根据部门Id获取详细信息
     */
    @RequiresPermissions("system:dept:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysDept dept) {
        return AjaxResult.success(deptService.selectDeptById(dept));
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), deptService.checkDeptCodeUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门编码已存在");
        } else if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), deptService.checkDeptCodeUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门编码已存在");
        } else if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId()) || StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), deptService.checkIsChild(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己或自己的子部门");
        } else if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), dept.getStatus())
                && deptService.checkNormalChildrenCount(dept) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 修改部门-角色关系
     */
    @RequiresPermissions("system:role:set")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeDeptRole")
    public AjaxResult editDeptRole(@Validated @RequestBody SysDept dept) {
        return toAjax(deptService.updateDeptRole(dept));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysDept dept) {

        if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), dept.getStatus())
                && deptService.checkNormalChildrenCount(dept) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        } else if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), dept.getStatus())
                && StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), deptService.checkDeptStatus(new SysDept(dept.getParentId())))) {
            return AjaxResult.error("启用失败，该部门的父级部门已被禁用！");
        }
        return toAjax(deptService.updateDeptStatus(dept));
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysDept dept) {
        if (deptService.hasChildByDeptId(dept)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistPost(dept)) {
            return AjaxResult.error("部门存在岗位,不允许删除");
        }
        if (deptService.checkDeptExistUser(dept)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(dept));
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