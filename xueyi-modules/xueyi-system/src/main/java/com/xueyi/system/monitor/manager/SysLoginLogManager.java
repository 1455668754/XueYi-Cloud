package com.xueyi.system.monitor.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.monitor.mapper.SysLoginLogMapper;
import org.springframework.stereotype.Component;

/**
 * 访问日志管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysLoginLogManager extends BaseManager<SysLoginLogDto, SysLoginLogMapper> {

    /**
     * 清空系统登录日志
     */
    public void cleanLoginLog(Long enterpriseId) {
        baseMapper.delete(
                Wrappers.<SysLoginLogDto>update().lambda()
                        .eq(SysLoginLogDto::getEnterpriseId, enterpriseId));
    }
}
