package com.xueyi.system.organize.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 组织-角色关联（角色绑定） 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysOrganizeRoleMergeMapper extends BaseMapper<SysOrganizeRoleMerge> {
}
