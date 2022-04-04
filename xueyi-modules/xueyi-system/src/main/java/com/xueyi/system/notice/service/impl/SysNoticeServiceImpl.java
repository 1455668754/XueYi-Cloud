package com.xueyi.system.notice.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.manager.SysNoticeManager;
import com.xueyi.system.notice.mapper.SysNoticeMapper;
import com.xueyi.system.notice.service.ISysNoticeService;
import org.springframework.stereotype.Service;

/**
 * 通知公告管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysNoticeServiceImpl extends BaseServiceImpl<SysNoticeDto, SysNoticeManager, SysNoticeMapper> implements ISysNoticeService {
}
