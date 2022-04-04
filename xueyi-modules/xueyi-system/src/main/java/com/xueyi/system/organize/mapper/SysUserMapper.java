package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;

/**
 * 用户管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysUserMapper extends BaseMapper<SysUserDto> {
}
