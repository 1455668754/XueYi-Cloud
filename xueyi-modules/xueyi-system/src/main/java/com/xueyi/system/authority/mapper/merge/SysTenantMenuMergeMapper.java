package com.xueyi.system.authority.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 租户-菜单关联 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysTenantMenuMergeMapper extends BaseMapper<SysTenantMenuMerge> {
}
