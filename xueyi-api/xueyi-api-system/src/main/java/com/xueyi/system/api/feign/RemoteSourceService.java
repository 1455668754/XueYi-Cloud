package com.xueyi.system.api.feign;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.api.feign.factory.RemoteSourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 数据源策略加载服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteSourceService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSourceFallbackFactory.class)
public interface RemoteSourceService {

    /**
     * 根据策略Id刷新策略缓存
     *
     * @param strategyId 策略Id
     *                       @param source         请求来源
     * @return 结果
     */
    @GetMapping(value = "/dataSource/refreshSource/{strategyId}")
    public R<Boolean> refreshSource(@PathVariable("strategyId") Long strategyId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}