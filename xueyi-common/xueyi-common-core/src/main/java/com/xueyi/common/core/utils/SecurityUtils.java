package com.xueyi.common.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.text.Convert;

/**
 * 权限获取工具类
 *
 * @author ruoyi
 */
public class SecurityUtils
{

    /**
     * 获取企业Id
     */
    public static Long getEnterpriseId()
    {
        return Convert.toLong(ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_ENTERPRISE_ID));
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName()
    {
        return ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_ENTERPRISE_NAME);
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId()
    {
        return Convert.toLong(ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID));
    }

    /**
     * 获取用户
     */
    public static String getUserName()
    {
        String userName = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        return ServletUtils.urlDecode(userName);
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType()
    {
        String userType = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_TYPE);
        return ServletUtils.urlDecode(userType);
    }

    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(SecurityConstants.TOKEN_AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 替换token前缀
     */
    public static String replaceTokenPrefix(String token)
    {
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX))
        {
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     *
     * @param userType 用户类型
     * @return 结果
     */
    public static boolean isAdmin(String userType)
    {
        return userType != null && userType.equals("00");
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
