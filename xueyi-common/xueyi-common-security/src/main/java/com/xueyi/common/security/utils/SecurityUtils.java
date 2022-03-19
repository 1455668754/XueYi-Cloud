package com.xueyi.common.security.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.model.LoginUser;
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
        return SecurityContextHolder.getEnterpriseId();
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName() {
        return SecurityContextHolder.getEnterpriseName();
    }

    /**
     * 获取租户权限标识
     */
    public static String getIsLessor() {
        return SecurityContextHolder.getIsLessor();
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUserName() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        return SecurityContextHolder.getUserType();
    }

    /**
     * 获取租户策略源
     */
    public static String getSourceName() {
        return SecurityContextHolder.getSourceName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
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
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        return StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)
                ? token.replaceFirst(TokenConstants.PREFIX, StrUtil.EMPTY)
                : token;
    }

    /**
     * 是否为空租户信息
     */
    public static boolean isEmptyTenant() {
        return ObjectUtil.equals(SecurityConstants.EMPTY_TENANT_ID, getEnterpriseId());
    }

    /**
     * 是否不为空租户信息
     */
    public static boolean isNotEmptyTenant() {
        return !isEmptyTenant();
    }

    /**
     * 是否为超管租户
     */
    public static boolean isAdminTenant() {
        return StringUtils.equals(AuthorityConstants.TenantType.ADMIN.getCode(), getIsLessor());
    }

    /**
     * 是否不为超管租户
     */
    public static boolean isNotAdminTenant() {
        return !isAdminTenant();
    }

    /**
     * 是否为超管用户
     */
    public static boolean isAdminUser() {
        return StringUtils.equals(AuthorityConstants.UserType.ADMIN.getCode(), getUserType());
    }

    /**
     * 是否不为超管用户
     */
    public static boolean isNotAdminUser() {
        return !isAdminUser();
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
