package com.xueyi.system.api.log.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.feign.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 日志服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLogService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

    /**
     * 保存系统日志
     *
     * @param operateLog   日志实体
     * @param enterpriseId 企业Id
     * @param sourceName   数据源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/operateLog")
    R<Boolean> saveOperateLog(@RequestBody SysOperateLogDto operateLog, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存访问记录
     *
     * @param loginInfo    访问实体
     * @param enterpriseId 企业Id
     * @param sourceName   数据源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/loginLog")
    R<Boolean> saveLoginInfo(@RequestBody SysLoginLogDto loginInfo, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}