package com.xueyi.system.api.authority.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.authority.feign.RemoteAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 权限服务 降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteAuthFallbackFactory implements FallbackFactory<RemoteAuthService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteAuthFallbackFactory.class);

    @Override
    public RemoteAuthService create(Throwable throwable) {
        log.error("权限服务调用失败:{}", throwable.getMessage());
        return new RemoteAuthService() {
            @Override
            public R<Long[]> getTenantAuthInner(Long enterpriseId, String sourceName, String source) {
                return R.fail("获取租户权限信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> addTenantAuthInner(Long[] authIds, Long enterpriseId, String sourceName, String source) {
                return R.fail("新增租户权限信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> editTenantAuthInner(Long[] authIds, Long enterpriseId, String sourceName, String source) {
                return R.fail("修改权限信息失败:" + throwable.getMessage());
            }
        };
    }
}