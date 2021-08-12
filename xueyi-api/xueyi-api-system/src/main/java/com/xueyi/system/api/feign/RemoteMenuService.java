package com.xueyi.system.api.feign;

import com.xueyi.common.core.constant.ServiceNameConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.api.feign.factory.RemoteMenuFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 租户菜单权限服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteMenuService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface RemoteMenuService {

    /**
     * 获取指定企业账号的租管衍生角色菜单范围信息
     *
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @GetMapping(value = "/enterprise/getMenuScope/{enterpriseId}")
    public R<List<SysRoleSystemMenu>> getMenuScope(@PathVariable("enterpriseId") Long enterpriseId);

    /**
     * 修改保存指定企业账号的租管衍生角色菜单权限
     *
     * @param enterpriseId 企业Id
     * @return 结果
     */
    @GetMapping(value = "/enterprise/authMenuScope/{enterpriseId}")
    public R<Integer> authMenuScope(@PathVariable("enterpriseId") Long enterpriseId);
}
