package com.xueyi.job.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;

/**
 * 调度任务管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysJobMapper extends SubBaseMapper<SysJobDto, SysJobLogDto> {
}
