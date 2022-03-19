package com.xueyi.tenant.source.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.common.web.entity.mapper.BaseMapper;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 数据源管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface TeSourceMapper extends BaseMapper<TeSourceDto> {
}