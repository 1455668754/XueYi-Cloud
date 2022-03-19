package com.xueyi.system.monitor.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;

/**
 * 访问日志管理 服务层
 *
 * @author xueyi
 */
public interface ISysLoginLogService extends IBaseService<SysLoginLogDto> {

    /**
     * 清空系统登录日志
     */
    void cleanLoginLog();
}
