package com.xueyi.system.notice.service;

import java.util.List;

import com.xueyi.system.notice.domain.SysNotice;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface ISysNoticeService {

    /**
     * 查询公告信息
     *
     * @param notice 公告信息 | noticeId 公告Id
     * @return 公告信息
     */
    public SysNotice selectNoticeById(SysNotice notice);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 删除公告信息
     *
     * @param notice 公告信息 | noticeId 公告Id
     * @return 结果
     */
    public int deleteNoticeById(SysNotice notice);

    /**
     * 批量删除公告信息
     *
     * @param notice 公告信息 | params.Ids 需要删除的公告Ids组
     * @return 结果
     */
    public int deleteNoticeByIds(SysNotice notice);
}
