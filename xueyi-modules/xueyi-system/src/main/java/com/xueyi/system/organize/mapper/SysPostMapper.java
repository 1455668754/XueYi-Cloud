package com.xueyi.system.organize.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 岗位管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysPostMapper extends BaseMapper<SysPostDto> {
}
