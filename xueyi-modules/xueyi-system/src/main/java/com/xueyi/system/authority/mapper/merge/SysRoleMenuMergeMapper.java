package com.xueyi.system.authority.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 角色-菜单关联 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysRoleMenuMergeMapper extends BaseMapper<SysRoleMenuMerge> {
}
