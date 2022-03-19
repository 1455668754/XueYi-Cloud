package com.xueyi.system.api.organize.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.feign.RemotePostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 岗位服务 降级处理
 *
 * @author xueyi
 */
@Component
public class RemotePostFallbackFactory implements FallbackFactory<RemotePostService> {

    private static final Logger log = LoggerFactory.getLogger(RemotePostFallbackFactory.class);

    @Override
    public RemotePostService create(Throwable throwable) {
        log.error("岗位服务调用失败:{}", throwable.getMessage());
        return new RemotePostService() {
            @Override
            public R<SysPostDto> addInner(SysPostDto post, Long enterpriseId, String sourceName, String source) {
                return R.fail("新增岗位失败:" + throwable.getMessage());
            }
        };
    }
}