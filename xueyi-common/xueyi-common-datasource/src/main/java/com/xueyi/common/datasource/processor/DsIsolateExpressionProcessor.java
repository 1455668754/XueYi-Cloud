package com.xueyi.common.datasource.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.model.LoginUser;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * 租户库源策略
 *
 * @author xueyi
 */
@Component
public class DsIsolateExpressionProcessor extends DsProcessor {

    private static final String ISOLATE_PREFIX = "#isolate";

    @Override
    public boolean matches(String key) {
        return key.startsWith(ISOLATE_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {

        TokenService tokenService = SpringUtils.getBean(TokenService.class);
        // 获取当前的用户
        LoginUser loginUser = tokenService.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            return loginUser.getMainSource();
//            //通过循环策略集获取主数据源 | 为改造主从提供支持
//            List<Source> sources = loginUser.getSource();
//            for (Source source: sources){
//                if (source.getIsMain().equals("Y")){
//                    return source.getMaster();
//                }
//            }
        }
        return null;
    }
}