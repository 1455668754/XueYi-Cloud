package com.xueyi.tenant.api.feign;

import com.xueyi.tenant.api.feign.factory.RemoteTenantFallbackFactory;
import com.xueyi.tenant.api.model.TenantRegister;
import org.springframework.cloud.openfeign.FeignClient;
import com.xueyi.common.core.constant.ServiceConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.domain.R;

/**
 * 用户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteTenantService", value = ServiceConstants.TENANT_SERVICE, fallbackFactory = RemoteTenantFallbackFactory.class)
public interface RemoteTenantService {

    /**
     * 注册租户信息
     *
     * @param register 注册信息
     * @param source   请求来源
     * @return 结果
     */
    @PostMapping("/tenant/register")
    public R<Boolean> registerTenantInfo(@RequestBody TenantRegister register, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}