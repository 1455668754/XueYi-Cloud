package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 模块Mapper接口
 *
 * @author xueyi
 */
public interface SysSystemMapper {

    /**
     * 查询模块信息列表
     * 访问控制 s 租户查询
     *
     * @param system 模块信息
     * @return 模块信息集合
     */
    @DataScope(edAlias = "s")
    public List<SysSystem> mainSelectSystemList(SysSystem system);

    /**
     * 查询模块信息
     * 访问控制 s 租户查询
     *
     * @param system 模块信息 | systemId 模块Id
     * @return 模块信息
     */
    @DataScope(edAlias = "s")
    public SysSystem mainSelectSystemById(SysSystem system);

    /**
     * 新增模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    public int mainInsertSystem(SysSystem system);

    /**
     * 修改模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    public int mainUpdateSystem(SysSystem system);

    /**
     * 修改模块状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system 模块信息
     * @return 结果
     */
    public int mainUpdateSystemStatus(SysSystem system);

    /**
     * 批量删除模块
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param system        模块信息 | params.Ids 需要删除的模块Ids组
     * @param isAdminTenant 是否租管租户
     * @return 结果
     */
    public int mainDeleteSystemByIds(@Param("system") SysSystem system, @Param("isAdminTenant") boolean isAdminTenant);

    /**
     * 查询角色Id存在于数组中的角色信息
     * 访问控制 s 租户查询
     *
     * @param system 模块信息 | params.Ids 模块Ids组
     * @return 结果
     */
    @DataScope(edAlias = "s")
    public Set<SysSystem> mainCheckSystemListByIds(SysSystem system);




























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

