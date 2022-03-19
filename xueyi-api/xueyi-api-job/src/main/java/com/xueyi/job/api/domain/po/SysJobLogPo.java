package com.xueyi.job.api.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;

/**
 * 调度日志 持久化对象
 *
 * @author xueyi
 */
public class SysJobLogPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 任务Id */
    @TableField("job_id")
    private Long jobId;

    /** 任务组名 */
    @Excel(name = "任务组名")
    @TableField("job_group")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    @TableField("invoke_target")
    private String invokeTarget;

    /** 调用租户字符串 */
    @TableField(value = "invoke_tenant")
    private String invokeTenant;

    /** 日志信息 */
    @Excel(name = "日志信息")
    @TableField("job_message")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    @TableField("status")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    @TableField("exception_info")
    private String exceptionInfo;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getInvokeTenant() {
        return invokeTenant;
    }

    public void setInvokeTenant(String invokeTenant) {
        this.invokeTenant = invokeTenant;
    }

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}
