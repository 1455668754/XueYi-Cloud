package com.xueyi.system.monitor.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;

/**
 * 操作日志管理 服务层
 *
 * @author xueyi
 */
public interface ISysOperateLogService extends IBaseService<SysOperateLogDto> {

    /**
     * 清空操作日志
     */
    void cleanOperateLog();
}
