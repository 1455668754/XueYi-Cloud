package com.xueyi.system.monitor.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.monitor.mapper.SysOperateLogMapper;
import org.springframework.stereotype.Component;

/**
 * 操作日志管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysOperateLogManager extends BaseManager<SysOperateLogDto, SysOperateLogMapper> {

    /**
     * 清空系统操作日志
     */
    public void cleanOperateLog() {
        baseMapper.delete(Wrappers.query());
    }

}
