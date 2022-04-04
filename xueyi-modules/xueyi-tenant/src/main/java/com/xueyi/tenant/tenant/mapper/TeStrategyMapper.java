package com.xueyi.tenant.tenant.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;

/**
 * 数据源策略管理 数据层
 *
 * @author xueyi
 */
@Master
public interface TeStrategyMapper extends BaseMapper<TeStrategyDto> {
}