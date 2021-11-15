package com.xueyi.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.xueyi.common.core.constant.ServiceConstants;
import org.springframework.web.bind.annotation.RequestHeader;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.feign.factory.RemoteUserFallbackFactory;
import com.xueyi.system.api.model.LoginUser;

/**
 * 用户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户信息
     *
     * @param enterpriseName 企业账号
     * @param userName       员工账号
     * @param source         请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{enterpriseName}/{userName}")
    public R<LoginUser> getUserInfo(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}