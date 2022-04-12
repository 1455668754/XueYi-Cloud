package com.xueyi.system.notice.service.impl;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.manager.SysNoticeManager;
import com.xueyi.system.notice.mapper.SysNoticeMapper;
import com.xueyi.system.notice.service.ISysNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知公告管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysNoticeServiceImpl extends BaseServiceImpl<SysNoticeDto, SysNoticeManager, SysNoticeMapper> implements ISysNoticeService {

    /**
     * 查询通知公告对象列表 | 数据权限 | 附加数据
     *
     * @param notice 通知公告对象
     * @return 通知公告对象集合
     */
    @Override
    @DataScope(userAlias = "createBy", mapperScope = {"SysNoticeMapper"})
    public List<SysNoticeDto> selectListScope(SysNoticeDto notice) {
        return baseManager.selectListExtra(notice);
    }
}
