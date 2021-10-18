package com.xueyi.system.api.feign.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xueyi.common.core.domain.R;
import org.springframework.stereotype.Component;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.feign.RemoteEnterpriseService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 企业信息服务降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteEnterpriseFallbackFactory implements FallbackFactory<RemoteEnterpriseService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteEnterpriseFallbackFactory.class);

    @Override
    public RemoteEnterpriseService create(Throwable throwable) {
        log.error("企业信息服务调用失败:{}", throwable.getMessage());
        return new RemoteEnterpriseService() {
            @Override
            public R<SysEnterprise> getEnterpriseByEnterpriseId(Long enterpriseId, String source) {
                return R.fail("获取企业信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> refreshEnterpriseAllCache(Long enterpriseId, String source) {
                return R.fail("更新企业缓存失败:" + throwable.getMessage());
            }
        };
    }
}