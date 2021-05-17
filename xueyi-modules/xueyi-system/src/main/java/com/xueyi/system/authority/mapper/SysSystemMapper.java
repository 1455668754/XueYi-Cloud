package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.authority.SysSystem;
import com.xueyi.system.api.utilTool.SysSearch;

import java.util.List;

/**
 * 子系统模块Mapper接口
 *
 * @author xueyi
 */
public interface SysSystemMapper {

    /**
     * 查询子系统模块列表|超管
     * 访问控制 s 租户查询
     *
     * @param search 查询组件 | null
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewAdminList(SysSearch search);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param search 查询组件 | userId 当前用户Id
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewList(SysSearch search);

    /**
     * 查询子系统模块
     * 访问控制 s 租户查询
     *
     * @param search 查询组件 | systemId 子系统模块Id
     * @return 子系统模块
     */
    @DataScope(eAlias = "s")
    public SysSystem selectSystemById(SysSearch search);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块集合
     */
    @DataScope(eAlias = "s")
    public List<SysSystem> selectSystemList(SysSystem sysSystem);

    /**
     * 新增子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertSystem(SysSystem sysSystem);

    /**
     * 修改子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystem(SysSystem sysSystem);

    /**
     * 修改子系统模块状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystemStatus(SysSystem sysSystem);

    /**
     * 批量删除子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | systemIds 需要删除的数据Ids
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSystemByIds(SysSearch search);
}

