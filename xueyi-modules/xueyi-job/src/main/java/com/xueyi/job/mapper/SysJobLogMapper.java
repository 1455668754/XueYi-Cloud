package com.xueyi.job.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.job.api.domain.dto.SysJobLogDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 调度日志管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysJobLogMapper extends BaseMapper<SysJobLogDto> {
}
