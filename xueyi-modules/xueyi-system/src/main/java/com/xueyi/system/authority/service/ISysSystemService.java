package com.xueyi.system.authority.service;

import com.xueyi.system.api.authority.SysSystem;

import java.util.List;

/**
 * 子系统模块Service接口
 *
 * @author xueyi
 */
public interface ISysSystemService {

    /**
     * 查询首页可展示子系统模块列表
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块集合
     */
    public List<SysSystem> selectSystemViewList(SysSystem sysSystem);

    /**
     * 查询子系统模块
     *
     * @param systemId 子系统模块Id
     * @return 子系统模块
     */
    public SysSystem selectSystemById(Long systemId);

    /**
     * 查询子系统模块列表
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块集合
     */
    public List<SysSystem> selectSystemList(SysSystem sysSystem);

    /**
     * 新增子系统模块
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    public int insertSystem(SysSystem sysSystem);

    /**
     * 修改子系统模块
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    public int updateSystem(SysSystem sysSystem);

    /**
     * 修改子系统模块状态
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    public int updateSystemStatus(SysSystem sysSystem);

    /**
     * 批量删除子系统模块
     *
     * @param systemIds 需要删除的子系统模块Id
     * @return 结果
     */
    public int deleteSystemByIds(Long[] systemIds);
}
