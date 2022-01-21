package com.xueyi.common.security.feign;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * feign 请求拦截器
 * 
 * @author ruoyi
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor
{
    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest))
        {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String enterpriseId = headers.get(SecurityConstants.Details.ENTERPRISE_ID.getCode());
            if (StrUtil.isNotBlank(enterpriseId))
                requestTemplate.header(SecurityConstants.Details.ENTERPRISE_ID.getCode(), enterpriseId);
            String enterpriseName = headers.get(SecurityConstants.Details.ENTERPRISE_NAME.getCode());
            if (StrUtil.isNotBlank(enterpriseName))
                requestTemplate.header(SecurityConstants.Details.ENTERPRISE_NAME.getCode(), enterpriseName);
            String isLessor = headers.get(SecurityConstants.Details.IS_LESSOR.getCode());
            if (StrUtil.isNotBlank(isLessor))
                requestTemplate.header(SecurityConstants.Details.IS_LESSOR.getCode(), isLessor);
            String userId = headers.get(SecurityConstants.Details.USER_ID.getCode());
            if (StrUtil.isNotBlank(userId))
                requestTemplate.header(SecurityConstants.Details.USER_ID.getCode(), userId);
            String userName = headers.get(SecurityConstants.Details.USER_NAME.getCode());
            if (StrUtil.isNotBlank(userName))
                requestTemplate.header(SecurityConstants.Details.USER_NAME.getCode(), userName);
            String userType = headers.get(SecurityConstants.Details.USER_TYPE.getCode());
            if (StrUtil.isNotBlank(userType))
                requestTemplate.header(SecurityConstants.Details.USER_TYPE.getCode(), userType);
            String sourceName = headers.get(SecurityConstants.Details.SOURCE_NAME.getCode());
            if (StrUtil.isNotBlank(sourceName))
                requestTemplate.header(SecurityConstants.Details.SOURCE_NAME.getCode(), sourceName);
            String authentication = headers.get(SecurityConstants.Details.AUTHORIZATION_HEADER.getCode());
            if (StrUtil.isNotBlank(authentication))
                requestTemplate.header(SecurityConstants.Details.AUTHORIZATION_HEADER.getCode(), authentication);
            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}