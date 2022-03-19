package com.xueyi.system.organize.controller;

import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/organize")
public class SysOrganizeController extends BasisController {

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 获取企业部门|岗位树
     */
    @GetMapping(value = "/organizeScope")
    @RequiresPermissions(value = {Auth.SYS_ROLE_ADD, Auth.SYS_ROLE_AUTH}, logical = Logical.OR)
    public AjaxResult getOrganizeScope() {
        return AjaxResult.success(TreeUtils.buildTree(organizeService.selectOrganizeScope()));
    }

    /**
     * 获取下拉树列表
     */
    @GetMapping("/option")
    public AjaxResult option() {
        return AjaxResult.success(organizeService.selectOrganizeTreeExDeptNode());
    }
}
