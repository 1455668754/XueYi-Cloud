package com.xueyi.system.api.organize.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.feign.factory.RemotePostFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 岗位服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remotePostService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemotePostFallbackFactory.class)
public interface RemotePostService {

    /**
     * 新增岗位
     *
     * @param post         岗位对象
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @param source       请求来源
     * @return 结果
     */
    @PostMapping("/post/inner/add")
    R<SysPostDto> addInner(@RequestBody SysPostDto post, @RequestHeader(SecurityConstants.ENTERPRISE_ID) Long enterpriseId, @RequestHeader(SecurityConstants.SOURCE_NAME) String sourceName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}