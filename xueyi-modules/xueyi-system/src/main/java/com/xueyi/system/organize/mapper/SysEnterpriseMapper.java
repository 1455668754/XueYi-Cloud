package com.xueyi.system.organize.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 企业管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysEnterpriseMapper extends BaseMapper<SysEnterpriseDto> {
}
