package com.xueyi.system.authority.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 租户-模块关联 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysTenantModuleMergeMapper extends BaseMapper<SysTenantModuleMerge> {
}
