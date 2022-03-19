package com.xueyi.system.organize.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 角色-部门关联（权限范围） 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysRoleDeptMergeMapper extends BaseMapper<SysRoleDeptMerge> {
}