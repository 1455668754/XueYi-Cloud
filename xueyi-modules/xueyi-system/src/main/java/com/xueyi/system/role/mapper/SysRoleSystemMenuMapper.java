package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.domain.SysRoleSystemMenu;

import java.util.List;

/**
 * 角色和系统-菜单关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleSystemMenuMapper {

    /**
     * 查询当前用户Id拥有权限的模块&菜单即可（构建前端路由 | 前端产品中心）
     * 访问控制 rsm 租户查询
     *
     * @param menu 菜单信息 | systemId 系统Id | params.userId 用户Id | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectSystemMenuListByUserId(SysMenu menu);

    /**
     * 仅获取超管衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectPermitAdministrator(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取租户衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectPermitEnterprise(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectPermitPersonalScreenDerive(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectPermitPersonal(SysRoleSystemMenu systemMenu);

    /**
     * 查询系统-菜单集合 | 仅获取没有子集的菜单或者系统信息
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | roleId 角色Id | menus 菜单组（List<SysMenu>） has menuId | systemId
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectMenuScopeByIdExclude(SysSearch search);

    /**
     * 查询系统-菜单使用数量
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public int checkSystemMenuExistRole(SysSearch search);

    /**
     * 批量新增角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | systemMenuIds 系统-菜单Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int batchRoleSystemMenu(SysSearch search);

    /**
     * 删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleSystemMenuByRoleId(SysSearch search);

    /**
     * 删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleSystemMenuBySystemMenuId(SysSearch search);

    /**
     * 批量删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteRoleSystemMenuByIds(SysSearch search);
}
