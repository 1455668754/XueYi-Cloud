package com.xueyi.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.utils.JwtUtils;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 *
 * @author xueyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userKey = JwtUtils.getUserKey(claims);
        boolean isLogin = redisService.hasKey(getTokenKey(userKey));
        if (!isLogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String enterpriseId = JwtUtils.getEnterpriseId(claims);
        String enterpriseName = JwtUtils.getEnterpriseName(claims);
        String isLessor = JwtUtils.getIsLessor(claims);
        String userId = JwtUtils.getUserId(claims);
        String userName = JwtUtils.getUserName(claims);
        String userType = JwtUtils.getUserType(claims);
        String sourceName = JwtUtils.getSourceName(claims);

        if (StrUtil.hasBlank(enterpriseId, enterpriseName, isLessor, userId, userName, userType, sourceName)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.ENTERPRISE_ID, enterpriseId);
        addHeader(mutate, SecurityConstants.ENTERPRISE_NAME, enterpriseName);
        addHeader(mutate, SecurityConstants.IS_LESSOR, isLessor);
        addHeader(mutate, SecurityConstants.USER_ID, userId);
        addHeader(mutate, SecurityConstants.USER_NAME, userName);
        addHeader(mutate, SecurityConstants.USER_TYPE, userType);
        addHeader(mutate, SecurityConstants.SOURCE_NAME, sourceName);
        addHeader(mutate, SecurityConstants.USER_KEY, userKey);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null)
            return;
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpConstants.Status.UNAUTHORIZED.getCode());
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}