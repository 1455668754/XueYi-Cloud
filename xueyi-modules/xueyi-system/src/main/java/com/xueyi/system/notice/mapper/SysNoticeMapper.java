package com.xueyi.system.notice.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.notice.domain.SysNotice;

/**
 * 通知公告表 数据层
 *
 * @author ruoyi
 */
public interface SysNoticeMapper
{
    /**
     * 查询公告信息
     * 访问控制 n 租户查询
     * @param search 万用组件 | noticeId 公告Id
     * @return 公告信息
     */
    @DataScope(eAlias = "n")
    public SysNotice selectNoticeById(SysSearch search);

    /**
     * 查询公告列表
     * 访问控制 n 租户查询
     * @param notice 公告信息
     * @return 公告集合
     */
    @DataScope(eAlias = "n")
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     * 访问控制 empty 租户更新（无前缀）
     * @param notice 公告信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     * 访问控制 empty 租户更新（无前缀）
     * @param notice 公告信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateNotice(SysNotice notice);

    /**
     * 批量删除公告
     * 访问控制 empty 租户更新（无前缀）
     * @param search 万用组件 | noticeId 公告Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteNoticeById(SysSearch search);

    /**
     * 批量删除公告信息
     * 访问控制 empty 租户更新（无前缀）
     * @param search 万用组件 | noticeIds 需要删除的公告Id(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteNoticeByIds(SysSearch search);
}
