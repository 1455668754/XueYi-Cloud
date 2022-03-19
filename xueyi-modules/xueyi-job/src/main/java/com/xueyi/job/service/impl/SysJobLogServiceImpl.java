package com.xueyi.job.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.manager.SysJobLogManager;
import com.xueyi.job.mapper.SysJobLogMapper;
import com.xueyi.job.service.ISysJobLogService;
import org.springframework.stereotype.Service;

/**
 * 调度日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysJobLogServiceImpl extends BaseServiceImpl<SysJobLogDto, SysJobLogManager, SysJobLogMapper> implements ISysJobLogService {

    /**
     * 清空任务日志
     */
    @Override
    public void cleanLog() {
        baseManager.cleanLog();
    }
}
