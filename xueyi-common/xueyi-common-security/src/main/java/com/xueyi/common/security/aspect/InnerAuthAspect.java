package com.xueyi.common.security.aspect;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.exception.InnerAuthException;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 内部服务调用验证处理
 *
 * @author xueyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String enterpriseId = ServletUtils.getRequest().getHeader(SecurityConstants.ENTERPRISE_ID);
        String enterpriseName = ServletUtils.getRequest().getHeader(SecurityConstants.ENTERPRISE_NAME);
        String isLessor = ServletUtils.getRequest().getHeader(SecurityConstants.IS_LESSOR);
        String userId = ServletUtils.getRequest().getHeader(SecurityConstants.USER_ID);
        String userName = ServletUtils.getRequest().getHeader(SecurityConstants.USER_NAME);
        String userType = ServletUtils.getRequest().getHeader(SecurityConstants.USER_TYPE);
        String sourceName = ServletUtils.getRequest().getHeader(SecurityConstants.SOURCE_NAME);
        // 用户信息验证
        if (innerAuth.isUser() && (StrUtil.hasBlank(enterpriseId, enterpriseName, isLessor, userId, userName, userType, sourceName))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
