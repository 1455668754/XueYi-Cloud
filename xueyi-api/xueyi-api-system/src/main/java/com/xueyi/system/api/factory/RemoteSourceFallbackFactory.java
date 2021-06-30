package com.xueyi.system.api.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.RemoteSourceService;
import com.xueyi.tenant.api.source.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 租户数据源加载服务降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteSourceFallbackFactory implements FallbackFactory<RemoteSourceService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSourceFallbackFactory.class);

    @Override
    public RemoteSourceService create(Throwable throwable) {
        log.error("租户数据源加载服务调用失败:{}", throwable.getMessage());
        return new RemoteSourceService() {
            @Override
            public R<List<Source>> getLoadDataSources(Long enterpriseId) {
                return R.fail("获取租户数据源失败:" + throwable.getMessage());
            }
        };
    }
}