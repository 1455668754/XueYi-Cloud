package com.xueyi.system.api.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.feign.RemoteEnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 租户服务降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteEnterpriseFallbackFactory implements FallbackFactory<RemoteEnterpriseService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteEnterpriseFallbackFactory.class);

    @Override
    public RemoteEnterpriseService create(Throwable throwable) {
        log.error("租户服务调用失败:{}", throwable.getMessage());
        return new RemoteEnterpriseService() {

            @Override
            public R<Boolean> refreshEnterpriseCache(SysEnterprise newEnterprise, String source) {
                return R.fail("企业key新增|更新cache失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshEnterpriseKey(String oldEnterpriseName, SysEnterprise newEnterprise, String source) {
                return R.fail("刷新企业cache的key失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> resetEnterpriseCache(String source) {
                return R.fail("重置企业缓存失败:" + throwable.getMessage());
            }
        };
    }
}