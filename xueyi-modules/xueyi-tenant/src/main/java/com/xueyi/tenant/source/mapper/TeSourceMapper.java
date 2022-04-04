package com.xueyi.tenant.source.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;

/**
 * 数据源管理 数据层
 *
 * @author xueyi
 */
@Master
public interface TeSourceMapper extends BaseMapper<TeSourceDto> {
}