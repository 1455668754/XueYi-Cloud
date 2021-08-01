package com.xueyi.common.datasource.config;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.xueyi.common.core.constant.TenantConstants;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * 返回主数据源策略
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
        return TenantConstants.MAIN_SOURCE;
    }
}