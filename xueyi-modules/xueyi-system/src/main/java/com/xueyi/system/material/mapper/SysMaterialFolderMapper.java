package com.xueyi.system.material.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;
import com.xueyi.system.api.file.domain.dto.SysMaterialFolderDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 素材分类管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysMaterialFolderMapper extends SubTreeMapper<SysMaterialFolderDto, SysMaterialDto> {
}
