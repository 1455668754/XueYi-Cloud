package com.xueyi.common.core.utils;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.text.Convert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限获取工具类
 *
 * @author xueyi
 */
public class SecurityUtils {

    /**
     * 获取企业Id
     */
    public static Long getEnterpriseId() {
        return Convert.toLong(ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_ENTERPRISE_ID));
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName() {
        return ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_ENTERPRISE_NAME);
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return Convert.toLong(ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID));
    }

    /**
     * 获取用户
     */
    public static String getUserName() {
        String userName = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        return ServletUtils.urlDecode(userName);
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        String userType = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_TYPE);
        return ServletUtils.urlDecode(userType);
    }

    /**
     * 获取租户权限标识
     */
    public static String getIsLessor() {
        String lessorType = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_IS_LESSOR);
        return ServletUtils.urlDecode(lessorType);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(SecurityConstants.TOKEN_AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            token = token.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为超管用户
     *
     * @return 结果
     */
    public static boolean isAdminUser() {
        return StringUtils.equals(AuthorityConstants.USER_TYPE_ADMIN, getUserType());
    }

    /**
     * 是否为超管租户
     *
     * @return 结果
     */
    public static boolean isAdminTenant() {
        return StringUtils.equals(AuthorityConstants.TENANT_TYPE_ADMIN, getIsLessor());
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
