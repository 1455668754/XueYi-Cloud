package com.xueyi.system.monitor.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 访问日志管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysLoginLogMapper extends BaseMapper<SysLoginLogDto> {
}
