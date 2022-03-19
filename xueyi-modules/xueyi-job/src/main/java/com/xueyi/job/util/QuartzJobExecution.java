package com.xueyi.job.util;

import com.xueyi.job.api.domain.dto.SysJobDto;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author xueyi
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, SysJobDto sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
