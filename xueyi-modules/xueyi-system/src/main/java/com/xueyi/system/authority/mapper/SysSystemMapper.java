package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysSystem;

import java.util.List;

/**
 * 模块Mapper接口
 *
 * @author xueyi
 */
public interface SysSystemMapper {

    /**
     * 当前用户首页可展示的模块信息列表 | 管理员
     * 访问控制 s 租户查询
     *
     * @param system 模块信息 | null
     * @return 模块信息集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> AdminHomePageView(SysSystem system);

    /**
     * 查询模块信息列表
     * 访问控制 s 租户查询
     *
     * @param system 模块信息 | params.roleSystemPerms 菜单Id组（List<SysRoleSystemMenu>）
     * @return 模块信息集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemViewList(SysSystem system);

    /**
     * 查询模块信息
     * 访问控制 s 租户查询
     *
     * @param system 模块信息 | systemId 模块Id
     * @return 模块信息
     */
    @DataScope(edAlias = "s")
    public SysSystem selectSystemById(SysSystem system);

    /**
     * 查询模块信息列表
     * 访问控制 s 租户查询
     *
     * @param system 模块信息
     * @return 模块信息集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> selectSystemList(SysSystem system);

    /**
     * 新增模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertSystem(SysSystem system);

    /**
     * 修改模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystem(SysSystem system);

    /**
     * 修改模块状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSystemStatus(SysSystem system);

    /**
     * 批量删除模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息 | params.Ids 需要删除的模块Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSystemByIds(SysSystem system);



    /**
     * 查询系统全部模块信息列表 | 遵循模块查询控制
     * 访问控制 m 租户查询
     *
     * @param system 模块信息 | status 菜单状态 | menu_id 排除的菜单Id | params.onlyList in list | params.excludeList not in list
     * @return 模块信息列表
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> buildSystemMenuTreeSelect(SysSystem system);

    /**
     * 查询系统全部模块信息列表 | 遵循模块查询控制 | 仅公共数据
     * 访问控制 m 租户查询
     *
     * @param system 模块信息 | status 菜单状态 | menu_id 排除的菜单Id | params.onlyList in list | params.excludeList not in list
     * @return 模块信息列表
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> buildSystemMenuTreeSelectOnlyPublic(SysSystem system);
}

