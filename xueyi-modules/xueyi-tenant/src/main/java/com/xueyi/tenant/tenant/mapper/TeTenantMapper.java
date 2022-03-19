package com.xueyi.tenant.tenant.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.common.web.entity.mapper.BaseMapper;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 租户管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface TeTenantMapper extends BaseMapper<TeTenantDto> {
}