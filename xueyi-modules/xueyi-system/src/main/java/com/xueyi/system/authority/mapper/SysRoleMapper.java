package com.xueyi.system.authority.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 岗位管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysRoleMapper extends BaseMapper<SysRoleDto> {
}
