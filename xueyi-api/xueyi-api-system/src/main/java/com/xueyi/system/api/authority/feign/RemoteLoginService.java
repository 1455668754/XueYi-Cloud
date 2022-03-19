package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.authority.feign.factory.RemoteLoginFallbackFactory;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 登录服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLoginService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLoginFallbackFactory.class)
public interface RemoteLoginService {

    /**
     * 通过企业账号查询企业登录信息
     *
     * @param enterpriseName 企业账号
     * @param source         请求来源
     * @return 结果
     */
    @GetMapping("/login/inner/enterpriseInfo/{enterpriseName}")
    R<LoginUser> getEnterpriseInfoInner(@PathVariable("enterpriseName") String enterpriseName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户名查询用户登录信息
     *
     * @param userName     员工账号
     * @param password     密码
     * @param enterpriseId 企业Id
     * @param isLessor     企业类型
     * @param sourceName   策略源
     * @param source       请求来源
     * @return 结果
     */
    @GetMapping("/login/inner/userInfo/{userName}/{password}")
    R<LoginUser> getUserInfoInner(@PathVariable("userName") String userName, @PathVariable("password") String password, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.IS_LESSOR) String isLessor, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
