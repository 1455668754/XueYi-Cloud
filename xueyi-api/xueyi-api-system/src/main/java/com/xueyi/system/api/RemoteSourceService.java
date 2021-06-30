package com.xueyi.system.api;

import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.tenant.api.source.Source;
import com.xueyi.system.api.factory.RemoteSourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 租户数据源加载服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteSourceService", value = ServiceNameConstants.TENANT_SERVICE, fallbackFactory = RemoteSourceFallbackFactory.class)
public interface RemoteSourceService {
    /**
     * 查询租户可加载数据源
     *
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @GetMapping(value = "/source/loadDataSources/{enterpriseId}")
    public R<List<Source>> getLoadDataSources(@PathVariable("enterpriseId") Long enterpriseId);
}