package com.xueyi.job.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.job.api.domain.dto.SysJobLogDto;

/**
 * 调度日志管理 服务层
 *
 * @author xueyi
 */
public interface ISysJobLogService extends IBaseService<SysJobLogDto> {

    /**
     * 清空任务日志
     */
    public void cleanLog();
}
