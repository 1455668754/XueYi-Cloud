package com.xueyi.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.monitor.SysOperLog;
import com.xueyi.system.api.factory.RemoteLogFallbackFactory;

/**
 * 日志服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {
    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @return 结果
     */
    @PostMapping("/operlog")
    R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog);

    /**
     * 保存访问记录
     *
     * @param sourceName     数据源名称
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userId         用户Id
     * @param userName       用户名称
     * @param status         状态
     * @param message        消息
     * @return 结果
     */
    @PostMapping("/loginInfo")
    R<Boolean> saveLoginInfo(@RequestParam("sourceName") String sourceName, @RequestParam("enterpriseId") Long enterpriseId, @RequestParam("enterpriseName") String enterpriseName, @RequestParam("userId") Long userId, @RequestParam("userName") String userName, @RequestParam("status") String status,
                             @RequestParam("message") String message);
}