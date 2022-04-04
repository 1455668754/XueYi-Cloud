package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;

/**
 * 部门管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysDeptMapper extends SubTreeMapper<SysDeptDto, SysPostDto> {
}
