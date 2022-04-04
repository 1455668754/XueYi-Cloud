package com.xueyi.system.authority.mapper.merge;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;

/**
 * 租户-模块关联 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysTenantModuleMergeMapper extends BaseMapper<SysTenantModuleMerge> {
}
