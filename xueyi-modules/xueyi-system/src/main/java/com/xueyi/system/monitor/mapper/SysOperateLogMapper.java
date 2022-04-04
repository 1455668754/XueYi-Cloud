package com.xueyi.system.monitor.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;

/**
 * 操作日志管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysOperateLogMapper extends BaseMapper<SysOperateLogDto> {
}
