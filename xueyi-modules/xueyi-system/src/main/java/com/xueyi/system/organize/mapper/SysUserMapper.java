package com.xueyi.system.organize.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 用户管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysUserMapper extends BaseMapper<SysUserDto> {
}
