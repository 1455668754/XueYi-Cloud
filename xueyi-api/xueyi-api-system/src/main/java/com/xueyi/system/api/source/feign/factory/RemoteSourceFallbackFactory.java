package com.xueyi.system.api.source.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.source.feign.RemoteSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 数据源策略加载服务 降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteSourceFallbackFactory implements FallbackFactory<RemoteSourceService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSourceFallbackFactory.class);

    @Override
    public RemoteSourceService create(Throwable throwable) {
        log.error("数据源策略加载服务调用失败:{}", throwable.getMessage());
        return new RemoteSourceService() {
            @Override
            public R<Boolean> refreshSourceCache(Long strategyId, String source) {
                return R.fail("更新数据源策略失败:" + throwable.getMessage());
            }
        };
    }
}