package com.xueyi.system.notice.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.NoticeConstants;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.service.ISysNoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 通知公告管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController<SysNoticeDto, ISysNoticeService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "通知公告";
    }

    /**
     * 查询通知公告列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_NOTICE_LIST)
    public AjaxResult list(SysNoticeDto notice) {
        return super.list(notice);
    }

    /**
     * 查询通知公告详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_NOTICE_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 通知公告导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_NOTICE_EXPORT)
    @Log(title = "通知公告管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysNoticeDto notice) {
        super.export(response, notice);
    }

    /**
     * 通知公告新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_NOTICE_ADD)
    @Log(title = "通知公告管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysNoticeDto notice) {
        return super.add(notice);
    }

    /**
     * 通知公告修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_NOTICE_EDIT)
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysNoticeDto notice) {
        return super.edit(notice);
    }

    /**
     * 通知公告修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_NOTICE_EDIT, Auth.SYS_NOTICE_ES}, logical = Logical.OR)
    @Log(title = "通知公告管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysNoticeDto notice) {
        return super.editStatus(notice);
    }

    /**
     * 通知公告批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_NOTICE_DELETE)
    @Log(title = "通知公告管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取通知公告选择框列表
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
    protected void AEHandleValidated(BaseConstants.Operate operate, SysNoticeDto notice) {
        // 初始化发送状态
        if(operate.isAdd())
            notice.setStatus(NoticeConstants.NoticeStatus.READY.getCode());
    }
}
