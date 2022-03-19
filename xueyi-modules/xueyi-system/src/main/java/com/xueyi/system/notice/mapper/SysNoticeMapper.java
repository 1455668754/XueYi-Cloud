package com.xueyi.system.notice.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 通知公告管理 数据层
 *
 * @author xueyi
 */
@DS(ISOLATE)
public interface SysNoticeMapper extends BaseMapper<SysNoticeDto> {
}
