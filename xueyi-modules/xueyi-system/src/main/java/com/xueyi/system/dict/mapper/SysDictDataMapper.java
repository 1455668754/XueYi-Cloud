package com.xueyi.system.dict.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;

/**
 * 字典数据管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysDictDataMapper extends BaseMapper<SysDictDataDto> {
}
