package com.xueyi.system.api.feign;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.xueyi.system.api.feign.factory.RemoteEnterpriseFallbackFactory;

/**
 * 企业信息服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteEnterpriseService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteEnterpriseFallbackFactory.class)
public interface RemoteEnterpriseService {

    /**
     * 根据租户Id查询企业信息
     *
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @GetMapping("/enterprise/byId/{enterpriseId}")
    public R<SysEnterprise> getEnterpriseByEnterpriseId(@PathVariable("enterpriseId") Long enterpriseId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}