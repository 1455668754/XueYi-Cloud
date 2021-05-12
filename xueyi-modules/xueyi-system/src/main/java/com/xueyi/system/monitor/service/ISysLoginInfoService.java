package com.xueyi.system.monitor.service;

import java.util.List;

import com.xueyi.system.monitor.domain.SysLoginInfo;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author xueyi
 */
public interface ISysLoginInfoService {
    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    public int insertLoginInfo(SysLoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志Id
     * @return 结果
     */
    public int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    public void cleanLoginInfo();
}
