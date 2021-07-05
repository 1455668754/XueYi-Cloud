package com.xueyi.system.organize.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.organize.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.xueyi.system.api.organize.SysPost;
import com.xueyi.system.organize.service.ISysPostService;

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
    @PreAuthorize(hasPermi = "system:post:list")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    /**
     * 根据岗位Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:post:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysPost post) {
        return AjaxResult.success(postService.selectPostById(post));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize(hasPermi = "system:post:add")
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
    @PreAuthorize(hasPermi = "system:post:edit")
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
    @PreAuthorize(hasPermi = "system:role:set")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changePostRole")
    public AjaxResult editPostRole(@Validated @RequestBody SysPost post) {
        return toAjax(postService.updatePostRole(post));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysPost post) {
        SysDept dept = new SysDept();
        dept.setDeptId(post.getDeptId());
        if (StringUtils.equals(UserConstants.POST_NORMAL, post.getStatus())
                && UserConstants.DEPT_DISABLE.equals(deptService.checkDeptStatus(dept))) {
            return AjaxResult.error("启用失败，该岗位的归属部门已被禁用！");
        }
        return toAjax(postService.updatePostStatus(post));
    }

    /**
     * 删除岗位
     */
    @PreAuthorize(hasPermi = "system:post:remove")
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
    @PreAuthorize(hasPermi = "system:post:export")
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