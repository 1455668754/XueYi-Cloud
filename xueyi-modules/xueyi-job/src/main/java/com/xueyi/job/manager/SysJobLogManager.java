package com.xueyi.job.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.mapper.SysJobLogMapper;
import org.springframework.stereotype.Component;

/**
 * 调度任务日志管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysJobLogManager extends BaseManager<SysJobLogDto, SysJobLogMapper> {

    /**
     * 清空任务日志
     */
    public void cleanLog() {
        baseMapper.delete(Wrappers.update());
    }
}
