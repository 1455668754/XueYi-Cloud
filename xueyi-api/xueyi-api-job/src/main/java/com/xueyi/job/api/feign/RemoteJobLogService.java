package com.xueyi.job.api.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.feign.factory.RemoteJobLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 调度日志服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteJobLogService", value = ServiceConstants.JOB_SERVICE, fallbackFactory = RemoteJobLogFallbackFactory.class)
public interface RemoteJobLogService {

    /**
     * 保存调度日志
     *
     * @param jobLog       调度日志实体
     * @param enterpriseId 企业Id
     * @param isLessor     企业类型
     * @param sourceName   数据源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/job/log")
    R<Boolean> saveJobLog(@RequestBody SysJobLogDto jobLog, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.IS_LESSOR) String isLessor, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}