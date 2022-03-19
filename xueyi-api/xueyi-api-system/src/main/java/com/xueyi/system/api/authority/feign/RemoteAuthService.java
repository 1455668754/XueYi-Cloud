package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.authority.feign.factory.RemoteAuthFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 权限服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteAuthService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAuthFallbackFactory.class)
public interface RemoteAuthService {

    /**
     * 新增租户权限
     *
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @param source       请求来源
     * @return 结果
     */
    @GetMapping("/auth/inner/getTenantAuth")
    R<Long[]> getTenantAuthInner(@RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 新增租户权限
     *
     * @param authIds      权限Ids
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/auth/inner/addTenantAuth")
    R<Boolean> addTenantAuthInner(@RequestBody Long[] authIds, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 修改租户权限
     *
     * @param authIds      权限Ids
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/auth/inner/editTenantAuth")
    R<Boolean> editTenantAuthInner(@RequestBody Long[] authIds, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}