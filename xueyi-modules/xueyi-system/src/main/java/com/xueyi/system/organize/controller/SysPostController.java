package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController {

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取岗位列表
     */
    @RequiresPermissions("system:post:list")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    /**
     * 根据岗位Id获取详细信息
     */
    @RequiresPermissions("system:post:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysPost post) {
        return AjaxResult.success(postService.selectPostById(post));
    }

    /**
     * 新增岗位
     */
    @RequiresPermissions("system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @RequiresPermissions("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.updatePost(post));
    }

    /**
     * 修改岗位-角色关系
     */
    @RequiresPermissions("system:role:set")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changePostRole")
    public AjaxResult editPostRole(@Validated @RequestBody SysPost post) {
        return toAjax(postService.updatePostRole(post));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysPost post) {
        SysDept dept = new SysDept(post.getDeptId());
        if (StringUtils.equals(UserConstants.POST_NORMAL, post.getStatus())
                && UserConstants.DEPT_DISABLE.equals(deptService.checkDeptStatus(dept))) {
            return AjaxResult.error("启用失败，该岗位的归属部门已被禁用！");
        }
        return toAjax(postService.updatePostStatus(post));
    }

    /**
     * 删除岗位
     */
    @RequiresPermissions("system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysPost post) {
        if (postService.checkPostExistUser(post)) {
            return AjaxResult.error("岗位存在用户,不允许删除");
        }
        return toAjax(postService.deletePostById(post));
    }

    /**
     * 导出岗位
     */
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:post:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPost post) throws IOException {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        util.exportExcel(response, list, "岗位数据");
    }

    /**
     * 获取部门/岗位下拉树列表
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect() {
        return AjaxResult.success(postService.buildDeptPostTreeSelect());
    }
}