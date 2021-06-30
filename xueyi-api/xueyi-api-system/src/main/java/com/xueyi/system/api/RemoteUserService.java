package com.xueyi.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.factory.RemoteUserFallbackFactory;
import com.xueyi.system.api.model.LoginUser;

/**
 * 用户服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param enterpriseName 企业账号
     * @param userName       员工账号
     * @return 结果
     */
    @GetMapping(value = "/user/info/{enterpriseName}/{userName}")
    public R<LoginUser> getUserInfo(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName);
}