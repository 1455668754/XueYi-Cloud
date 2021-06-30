package com.xueyi.common.datasource.config;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.tenant.api.source.Source;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DsShiroExpressionProcessor extends DsProcessor {

    private static final String ISOLATE_PREFIX = "#isolate";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean matches(String key) {
        return key.startsWith(ISOLATE_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {

        // 获取当前的用户
        LoginUser loginUser = tokenService.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            List<Source> sources = loginUser.getSource();
            for (Source source: sources){
                System.out.println(source.getMaster());
                if (source.getIsMain().equals("Y")){
                    return source.getMaster();
                }
            }
        }
        return null;
    }
}
