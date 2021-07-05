package com.xueyi.system.notice.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.system.notice.domain.SysNotice;
import com.xueyi.system.notice.mapper.SysNoticeMapper;
import com.xueyi.system.notice.service.ISysNoticeService;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Service
@DS("#isolate")
public class SysNoticeServiceImpl implements ISysNoticeService {

    @Autowired
    private SysNoticeMapper noticeMapper;

    /**
     * 查询公告信息
     *
     * @param notice 公告信息 | noticeId 公告Id
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(SysNotice notice) {
        return noticeMapper.selectNoticeById(notice);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice) {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice) {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param notice 公告信息 | noticeId 公告Id
     * @return 结果
     */
    @Override
    public int deleteNoticeById(SysNotice notice) {
        return noticeMapper.deleteNoticeById(notice);
    }

    /**
     * 批量删除公告信息
     *
     * @param notice 公告信息 | params.Ids 需要删除的公告Ids组
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(SysNotice notice) {
        return noticeMapper.deleteNoticeByIds(notice);
    }
}
