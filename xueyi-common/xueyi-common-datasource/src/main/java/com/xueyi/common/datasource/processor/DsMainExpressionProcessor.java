package com.xueyi.common.datasource.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.xueyi.common.core.constant.TenantConstants;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * 主库源策略
 *
 * @author xueyi
 */
@Component
public class DsMainExpressionProcessor extends DsProcessor {

    private static final String ISOLATE_PREFIX = "#main";

    @Override
    public boolean matches(String key) {
        return key.startsWith(ISOLATE_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        return TenantConstants.Source.MAIN.getCode();
    }
}