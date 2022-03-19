package com.xueyi.job.util;

import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.job.ScheduleConstants;
import com.xueyi.common.core.utils.ExceptionUtil;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.bean.BeanUtils;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.feign.RemoteJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author xueyi
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysJobDto sysJob = new SysJobDto();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, sysJob);
            if (ObjectUtil.isNotNull(sysJob))
                doExecute(context, sysJob);
            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, SysJobDto sysJob) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param job  系统计划任务
     */
    protected void after(JobExecutionContext context, SysJobDto job, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLogDto jobLog = new SysJobLogDto();
        jobLog.setJobId(job.getId());
        jobLog.setName(job.getName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        jobLog.setInvokeTenant(job.getInvokeTenant());
        jobLog.setStartTime(startTime);
        jobLog.setStopTime(new Date());
        long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
        jobLog.setJobMessage(jobLog.getName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            jobLog.setStatus(DictConstants.DicStatus.FAIL.getCode());
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            jobLog.setExceptionInfo(errorMsg);
        } else {
            jobLog.setStatus(DictConstants.DicStatus.NORMAL.getCode());
        }
        String[] methodParams = jobLog.getInvokeTenant().split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        String enterpriseIdStr = StringUtils.trimToEmpty(methodParams[0]);
        Long enterpriseId = Long.valueOf(StringUtils.substring(enterpriseIdStr, 0, enterpriseIdStr.length() - 1));
        String isLessorStr = StringUtils.trimToEmpty(methodParams[1]);
        String isLessor = StringUtils.substring(isLessorStr, 1, isLessorStr.length() - 1);
        String sourceNameStr = StringUtils.trimToEmpty(methodParams[2]);
        String sourceName = StringUtils.substring(sourceNameStr, 1, sourceNameStr.length() - 1);
        // 写入数据库当中
        SpringUtils.getBean(RemoteJobLogService.class).saveJobLog(jobLog, enterpriseId, isLessor, sourceName, SecurityConstants.INNER);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJobDto sysJob) throws Exception;
}