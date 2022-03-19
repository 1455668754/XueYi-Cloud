package com.xueyi.system.dict.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 参数配置管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysConfigMapper extends BaseMapper<SysConfigDto> {
}