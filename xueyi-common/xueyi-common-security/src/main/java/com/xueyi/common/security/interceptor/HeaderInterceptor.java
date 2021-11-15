package com.xueyi.common.security.interceptor;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author xueyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setEnterpriseId(ServletUtils.getHeader(request, SecurityConstants.Details.ENTERPRISE_ID.getCode()));
        SecurityContextHolder.setEnterpriseName(ServletUtils.getHeader(request, SecurityConstants.Details.ENTERPRISE_NAME.getCode()));
        SecurityContextHolder.setIsLessor(ServletUtils.getHeader(request, SecurityConstants.Details.IS_LESSOR.getCode()));
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.Details.USER_ID.getCode()));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.Details.USER_NAME.getCode()));
        SecurityContextHolder.setUserType(ServletUtils.getHeader(request, SecurityConstants.Details.USER_TYPE.getCode()));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.Details.USER_KEY.getCode()));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.Details.LOGIN_USER.getCode(), loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}