package com.xueyi.job.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.job.ScheduleConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.exception.job.TaskException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.utils.CronUtils;
import com.xueyi.job.service.ISysJobService;
import com.xueyi.job.util.ScheduleUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 调度任务管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/job")
public class SysJobController extends BasisController {

    @Autowired
    private ISysJobService baseService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "调度任务";
    }

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    @RequiresPermissions(Auth.SCHEDULE_JOB_LIST)
    public AjaxResult list(SysJobDto job) {
        startPage();
        List<SysJobDto> list = baseService.selectListScope(job);
        return getDataTable(list);
    }

    /**
     * 查询调度任务详细
     */
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_SINGLE)
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(baseService.selectById(id));
    }

    /**
     * 调度任务导出
     */
    @PostMapping("/export")
    @RequiresPermissions(Auth.SCHEDULE_JOB_EXPORT)
    public void export(HttpServletResponse response, SysJobDto job) {
        List<SysJobDto> list = baseService.selectListScope(job);
        ExcelUtil<SysJobDto> util = new ExcelUtil<>(SysJobDto.class);
        util.exportExcel(response, list, "定时任务");
    }

    /**
     * 调度任务新增
     */
    @PostMapping
    @RequiresPermissions(Auth.SCHEDULE_JOB_ADD)
    @Log(title = "调度任务管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysJobDto job) throws SchedulerException, TaskException {
        AEHandleValidated(BaseConstants.Operate.ADD, job);
        return toAjax(baseService.insert(job));
    }

    /**
     * 调度任务修改
     */
    @PutMapping
    @RequiresPermissions(Auth.SCHEDULE_JOB_EDIT)
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysJobDto job) throws SchedulerException, TaskException {
        AEHandleValidated(BaseConstants.Operate.EDIT, job);
        return toAjax(baseService.update(job));
    }

    /**
     * 调度任务修改状态
     */
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SCHEDULE_JOB_EDIT, Auth.SCHEDULE_JOB_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "调度任务管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysJobDto job) throws SchedulerException {
        return toAjax(baseService.updateStatus(job.getId(), job.getStatus()));
    }

    /**
     * 定时任务立即执行一次
     */
    @GetMapping("/run/{id}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_EDIT)
    @Log(title = "定时任务 - 执行一次", businessType = BusinessType.UPDATE)
    public AjaxResult run(@PathVariable Long id) throws SchedulerException {
        baseService.run(id);
        return AjaxResult.success();
    }

    /**
     * 调度任务批量删除
     */
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SCHEDULE_JOB_DELETE)
    @Log(title = "调度任务管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) throws SchedulerException {
        if (CollUtil.isEmpty(idList))
            throw new ServiceException(StrUtil.format("无待删除{}！", getNodeName()));
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 获取调度任务选择框列表
     */
    @GetMapping("/option")
    public AjaxResult option() {
        return list(null);
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    protected void AEHandleValidated(BaseConstants.Operate operate, SysJobDto job) {
        if (!CronUtils.isValid(job.getCronExpression()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，Cron表达式不正确", operate.getInfo(), getNodeName(), job.getName()));
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), HttpConstants.Type.LOOKUP_RMI.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，目标字符串不允许'rmi'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.LOOKUP_LDAP.getCode(), HttpConstants.Type.LOOKUP_LDAPS.getCode()}))
            throw new ServiceException(StrUtil.format("{}{}{}失败，目标字符串不允许'ldap(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{HttpConstants.Type.HTTP.getCode(), HttpConstants.Type.HTTPS.getCode()}))
            throw new ServiceException(StrUtil.format("{}{}{}失败，目标字符串不允许'http(s)'调用", operate.getInfo(), getNodeName(), job.getName()));
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), ScheduleConstants.JOB_ERROR_STR))
            throw new ServiceException(StrUtil.format("{}{}{}失败，目标字符串存在违规", operate.getInfo(), getNodeName(), job.getName()));
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，目标字符串不在白名单内", operate.getInfo(), getNodeName(), job.getName()));
    }
}