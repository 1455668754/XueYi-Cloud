package com.xueyi.system.organize.mapper.merge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 用户-岗位关联 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysUserPostMergeMapper extends BaseMapper<SysUserPostMerge> {
}
