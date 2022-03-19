package com.xueyi.system.monitor.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 操作日志管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysOperateLogMapper extends BaseMapper<SysOperateLogDto> {
}
