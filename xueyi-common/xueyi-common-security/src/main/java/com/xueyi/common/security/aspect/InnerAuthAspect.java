package com.xueyi.common.security.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.exception.InnerAuthException;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.annotation.InnerAuth;

/**
 * 内部服务调用验证处理
 *
 * @author xueyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered
{
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable
    {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source))
        {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String enterpriseId = ServletUtils.getRequest().getHeader(SecurityConstants.Details.ENTERPRISE_ID.getCode());
        String enterpriseName = ServletUtils.getRequest().getHeader(SecurityConstants.Details.ENTERPRISE_NAME.getCode());
        String userId = ServletUtils.getRequest().getHeader(SecurityConstants.Details.USER_ID.getCode());
        String userName = ServletUtils.getRequest().getHeader(SecurityConstants.Details.USER_NAME.getCode());
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(enterpriseId) || StringUtils.isEmpty(enterpriseName)) && (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName)))
        {
            throw new InnerAuthException("没有设置用户信息，不允许访问");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
