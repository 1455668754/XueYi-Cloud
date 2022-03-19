package com.xueyi.job.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.job.api.domain.po.SysJobLogPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 调度日志 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_job_log", excludeProperty = {"createBy","updateBy","remark","updateTime","sort"})
public class SysJobLogDto extends SysJobLogPo {

    private static final long serialVersionUID = 1L;

    /** 开始时间 */
    @TableField(exist = false)
    private Date startTime;

    /** 停止时间 */
    @TableField(exist = false)
    private Date stopTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getStopTime()
    {
        return stopTime;
    }

    public void setStopTime(Date stopTime)
    {
        this.stopTime = stopTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("jobGroup", getJobGroup())
                .append("invokeTarget", getInvokeTarget())
                .append("jobMessage", getJobMessage())
                .append("status", getStatus())
                .append("exceptionInfo", getExceptionInfo())
                .append("createTime", getCreateTime())
                .append("startTime", getStartTime())
                .append("stopTime", getStopTime())
                .toString();
    }
}
