package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;

/**
 * 角色管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysModuleMapper extends SubBaseMapper<SysModuleDto, SysMenuDto> {
}
