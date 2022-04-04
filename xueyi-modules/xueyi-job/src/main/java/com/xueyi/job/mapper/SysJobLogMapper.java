package com.xueyi.job.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.job.api.domain.dto.SysJobLogDto;

/**
 * 调度日志管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysJobLogMapper extends BaseMapper<SysJobLogDto> {
}
