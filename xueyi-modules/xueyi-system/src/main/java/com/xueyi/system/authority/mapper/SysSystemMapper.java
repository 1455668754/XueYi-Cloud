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
     * 当前用户首页可展示的模块列表 | 管理员
     * 访问控制 s 租户查询
     *
     * @param system 子系统模块 | null
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> AdminHomePageView(SysSystem system);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param system 子系统模块 | params.roleSystemPerms 菜单Id组（List<SysRoleSystemMenu>）
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewList(SysSystem system);

    /**
     * 查询子系统模块
     * 访问控制 s 租户查询
     *
     * @param system 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    @DataScope(edAlias = "s")
    public SysSystem selectSystemById(SysSystem system);

    /**
     * 查询子系统模块列表
     * 访问控制 s 租户查询
     *
     * @param system 子系统模块
     * @return 子系统模块集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemList(SysSystem system);

    /**
     * 查询系统全部模块列表 | 遵循模块查询控制
     * 访问控制 m 租户查询
     *
     * @param system 模块模块 | status 菜单状态 | menu_id 排除的菜单Id | params.onlyList in list | params.excludeList not in list
     * @return 模块列表
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> buildSystemMenuTreeSelect(SysSystem system);

    /**
     * 查询系统全部模块列表 | 遵循模块查询控制 | 仅公共数据
     * 访问控制 m 租户查询
     *
     * @param system 模块模块 | status 菜单状态 | menu_id 排除的菜单Id | params.onlyList in list | params.excludeList not in list
     * @return 模块列表
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> buildSystemMenuTreeSelectOnlyPublic(SysSystem system);

    /**
     * 新增子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertSystem(SysSystem system);

    /**
     * 修改子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystem(SysSystem system);

    /**
     * 修改子系统模块状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 子系统模块
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystemStatus(SysSystem system);

    /**
     * 批量删除子系统模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSystemByIds(SysSystem system);
}

