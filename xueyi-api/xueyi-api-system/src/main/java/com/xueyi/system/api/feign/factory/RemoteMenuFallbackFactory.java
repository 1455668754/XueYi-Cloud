package com.xueyi.system.api.feign.factory;

import com.xueyi.common.core.domain.R;
import com.xueyi.system.api.feign.RemoteMenuService;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 租户菜单权限服务降级处理
 *
 * @author xueyi
 */
@Component
public class RemoteMenuFallbackFactory implements FallbackFactory<RemoteMenuService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMenuFallbackFactory.class);

    @Override
    public RemoteMenuService create(Throwable throwable) {
        log.error("租户菜单权限服务降级处理调用失败:{}", throwable.getMessage());
        return new RemoteMenuService() {

            @Override
            public R<List<SysRoleSystemMenu>> getMenuScope(Long enterpriseId) {
                return R.fail("获取租管衍生角色菜单范围信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> authMenuScope(Long enterpriseId) {
                return R.fail("保存租管衍生角色菜单权限失败:" + throwable.getMessage());
            }
        };
    }
}