package com.xueyi.system.organize.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 部门管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysDeptMapper extends SubTreeMapper<SysDeptDto, SysPostDto> {
}
