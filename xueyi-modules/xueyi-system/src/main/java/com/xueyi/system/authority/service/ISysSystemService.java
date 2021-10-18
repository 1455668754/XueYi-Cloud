package com.xueyi.system.authority.service;

import com.xueyi.system.api.domain.authority.SysSystem;

import java.util.List;
import java.util.Set;

/**
 * 子系统模块Service接口
 *
 * @author xueyi
 */
public interface ISysSystemService {

    /**
     * 当前用户首页可展示的模块列表
     *
     * @return 子系统模块集合
     */
    public List<SysSystem> getSystemRoutes();

    /**
     * 查询子系统模块列表
     *
     * @param system 子系统模块
     * @return 子系统模块集合
     */
    public List<SysSystem> mainSelectSystemList(SysSystem system);

    /**
     * 查询子系统模块
     *
     * @param system 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    public SysSystem mainSelectSystemById(SysSystem system);

    /**
     * 新增子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int mainInsertSystem(SysSystem system);

    /**
     * 修改子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int mainUpdateSystem(SysSystem system);

    /**
     * 修改子系统模块状态
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int mainUpdateSystemStatus(SysSystem system);

    /**
     * 批量删除子系统模块
     *
     * @param system 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    public int mainDeleteSystemByIds(SysSystem system);

    /**
     * 查询角色Id存在于数组中的角色信息
     *
     * @param system 模块信息 | params.Ids 模块Ids组
     * @return 结果
     */
    public Set<SysSystem> mainCheckSystemListByIds(SysSystem system);
}
