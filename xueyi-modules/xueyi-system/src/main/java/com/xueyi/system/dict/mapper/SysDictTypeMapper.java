package com.xueyi.system.dict.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;

/**
 * 字典类型管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysDictTypeMapper extends SubBaseMapper<SysDictTypeDto, SysDictDataDto> {
}
