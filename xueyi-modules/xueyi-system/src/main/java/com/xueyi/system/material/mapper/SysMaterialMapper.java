package com.xueyi.system.material.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 素材管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysMaterialMapper extends BaseMapper<SysMaterialDto> {
}
