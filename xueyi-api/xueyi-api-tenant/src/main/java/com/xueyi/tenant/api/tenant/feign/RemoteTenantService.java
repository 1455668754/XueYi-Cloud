package com.xueyi.tenant.api.tenant.feign;

import cn.hutool.json.JSONObject;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.tenant.api.tenant.feign.factory.RemoteTenantFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
     * @param register 注册信息 | 约定json内容tenant = tenant, dept = dept, post = post, user = user
     * @param source   请求来源
     * @return 结果
     */
    @PostMapping("/tenant/register")
    public R<Boolean> registerTenantInfo(@RequestBody JSONObject register, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}