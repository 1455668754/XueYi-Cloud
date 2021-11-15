package com.xueyi.system.api.feign;

import com.xueyi.system.api.feign.factory.RemoteConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.xueyi.common.core.constant.ServiceConstants;
import com.xueyi.common.core.domain.R;

/**
 * 参数服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteConfigService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteConfigFallbackFactory.class)
public interface RemoteConfigService {

    /**
     * 查询参数
     *
     * @param configKey 参数键名
     * @return 结果
     */
    @GetMapping("/config/innerConfigKey/{configKey}")
    public R<String> getKey(@PathVariable("configKey") String configKey);
}