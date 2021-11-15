package com.xueyi.common.security.service;

import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.utils.*;
import com.xueyi.common.core.utils.ip.IpUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author xueyi
 */
@Component
public class TokenService {

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();

        Long enterpriseId = loginUser.getSysEnterprise().getEnterpriseId();
        String enterpriseName = loginUser.getSysEnterprise().getEnterpriseName();
        String isLessor = loginUser.getSysEnterprise().getIsLessor();
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        String userType = loginUser.getSysUser().getUserType();

        loginUser.setToken(token);
        loginUser.setEnterpriseId(enterpriseId);
        loginUser.setEnterpriseName(enterpriseName);
        loginUser.setIsLessor(isLessor);
        loginUser.setUserId(userId);
        loginUser.setUserName(userName);
        loginUser.setUserType(userType);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.Details.USER_KEY.getCode(), token);
        claimsMap.put(SecurityConstants.Details.ENTERPRISE_ID.getCode(), enterpriseId);
        claimsMap.put(SecurityConstants.Details.ENTERPRISE_NAME.getCode(), enterpriseName);
        claimsMap.put(SecurityConstants.Details.IS_LESSOR.getCode(), isLessor);
        claimsMap.put(SecurityConstants.Details.USER_ID.getCode(), userId);
        claimsMap.put(SecurityConstants.Details.USER_NAME.getCode(), userName);
        claimsMap.put(SecurityConstants.Details.USER_TYPE.getCode(), userType);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheObject(getTokenKey(userKey));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser 用户信息
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}
