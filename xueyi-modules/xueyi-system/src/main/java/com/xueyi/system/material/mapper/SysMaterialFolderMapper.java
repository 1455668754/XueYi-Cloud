package com.xueyi.system.material.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;
import com.xueyi.system.api.file.domain.dto.SysMaterialFolderDto;

/**
 * 素材分类管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysMaterialFolderMapper extends SubTreeMapper<SysMaterialFolderDto, SysMaterialDto> {
}
