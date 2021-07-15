package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysSystem;

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
     * @param sysSystem 子系统模块 | null
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewAdminList(SysSystem sysSystem);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param sysSystem 子系统模块 | params.roleSystemPerms 菜单Id组（List<SysRoleSystemMenu>）
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewList(SysSystem sysSystem);

    /**
     * 查询子系统模块
     * 访问控制 s 租户查询
     *
     * @param sysSystem 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    @DataScope(edAlias = "s")
    public SysSystem selectSystemById(SysSystem sysSystem);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
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
     * @param sysSystem 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSystemByIds(SysSystem sysSystem);
}

