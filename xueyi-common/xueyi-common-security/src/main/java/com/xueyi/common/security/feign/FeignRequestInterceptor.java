package com.xueyi.common.security.feign;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

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
            String userId = headers.get(SecurityConstants.Details.USER_ID.getCode());
            if (StringUtils.isNotEmpty(userId))
            {
                requestTemplate.header(SecurityConstants.Details.USER_ID.getCode(), userId);
            }
            String userName = headers.get(SecurityConstants.Details.USER_NAME.getCode());
            if (StringUtils.isNotEmpty(userName))
            {
                requestTemplate.header(SecurityConstants.Details.USER_NAME.getCode(), userName);
            }
            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
            if (StringUtils.isNotEmpty(authentication))
            {
                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}