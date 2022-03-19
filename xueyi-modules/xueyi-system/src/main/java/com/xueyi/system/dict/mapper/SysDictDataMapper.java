package com.xueyi.system.dict.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 字典数据管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysDictDataMapper extends BaseMapper<SysDictDataDto> {
}
