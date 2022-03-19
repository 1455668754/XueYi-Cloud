package com.xueyi.system.source.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.source.domain.Source;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 策略组管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysSourceMapper extends BaseMapper<Source> {
}
