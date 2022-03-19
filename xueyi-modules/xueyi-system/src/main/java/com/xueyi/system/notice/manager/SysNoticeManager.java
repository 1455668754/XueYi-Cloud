package com.xueyi.system.notice.manager;

import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.mapper.SysNoticeMapper;
import org.springframework.stereotype.Component;

/**
 * 通知公告管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysNoticeManager extends BaseManager<SysNoticeDto, SysNoticeMapper> {
}
