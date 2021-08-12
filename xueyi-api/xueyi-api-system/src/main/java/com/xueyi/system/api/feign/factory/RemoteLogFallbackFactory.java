package com.xueyi.system.api.feign.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.feign.RemoteLogService;
import com.xueyi.system.api.domain.monitor.SysLoginInfo;
import com.xueyi.system.api.domain.monitor.SysOperLog;

/**
 * 日志服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return null;
            }

            @Override
            public R<Boolean> saveLoginInfo(SysLoginInfo loginInfo, String source) {
                return null;
            }
        };
    }
}