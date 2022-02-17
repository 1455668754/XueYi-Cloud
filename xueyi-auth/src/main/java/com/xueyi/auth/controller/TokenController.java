package com.xueyi.auth.controller;

import com.xueyi.auth.form.LoginBody;
import com.xueyi.auth.form.RegisterBody;
import com.xueyi.auth.service.SysLoginService;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.JwtUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author xueyi
 */
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getEnterpriseName(), form.getUserName(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String sourceName =JwtUtils.getSourceName(token);
            Long enterpriseId = Long.valueOf(JwtUtils.getEnterpriseId(token));
            String enterpriseName =JwtUtils.getEnterpriseName(token);
            Long userId = Long.valueOf(JwtUtils.getUserId(token));
            String userName =JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(sourceName, enterpriseId, enterpriseName, userId, userName);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }
}