package com.xueyi.system.dict.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 字典类型管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysDictTypeMapper extends SubBaseMapper<SysDictTypeDto, SysDictDataDto> {
}
