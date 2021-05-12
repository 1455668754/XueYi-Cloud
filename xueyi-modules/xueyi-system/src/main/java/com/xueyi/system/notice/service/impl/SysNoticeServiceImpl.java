package com.xueyi.system.notice.service.impl;

import java.util.List;

import com.xueyi.system.api.utilTool.SysSearch;
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
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper noticeMapper;

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        SysSearch search = new SysSearch();
        search.getSearch().put("noticeId", noticeId);
        return noticeMapper.selectNoticeById(search);//@param search 万用组件 | noticeId 公告Id
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        SysSearch search = new SysSearch();
        search.getSearch().put("noticeId", noticeId);
        return noticeMapper.deleteNoticeById(search);//@param search 万用组件 | noticeId 公告Id
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        SysSearch search = new SysSearch();
        search.getSearch().put("noticeIds", noticeIds);
        return noticeMapper.deleteNoticeByIds(search);//@param search 万用组件 | noticeIds 需要删除的公告Id(Long[])
    }
}
