package com.xueyi.job.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 调度任务管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysJobMapper extends SubBaseMapper<SysJobDto, SysJobLogDto> {
}
