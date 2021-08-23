package com.xueyi.system.api.feign;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.feign.factory.RemoteEnterpriseFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 租户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteEnterpriseService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteEnterpriseFallbackFactory.class)
public interface RemoteEnterpriseService {

    /**
     * 根据企业 key 新增|更新 cache
     *
     * @param newEnterprise 企业对象
     */
    @GetMapping(value = "/enterprise/refreshEnterpriseCache")
    public R<Boolean> refreshEnterpriseCache(SysEnterprise newEnterprise, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 刷新指定企业 cache 的 key
     *
     * @param oldEnterpriseName 原企业账号
     * @param newEnterprise     企业对象
     */
    @GetMapping(value = "/enterprise/refreshEnterpriseKey/{oldEnterpriseName}")
    public R<Boolean> refreshEnterpriseKey(@PathVariable("oldEnterpriseName") String oldEnterpriseName, SysEnterprise newEnterprise, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 重置企业缓存数据
     */
    @GetMapping(value = "/enterprise/resetEnterpriseCache")
    public R<Boolean> resetEnterpriseCache(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}