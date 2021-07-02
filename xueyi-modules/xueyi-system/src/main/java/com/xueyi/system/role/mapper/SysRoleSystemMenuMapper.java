package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.role.domain.SysRoleSystemMenu;

import java.util.List;

/**
 * 角色和系统-菜单关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleSystemMenuMapper {
    /**
     * 获取菜单数据权限（登录校验）
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | userId 用户Id | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectSystemMenuPermsList(SysSearch search);

    /**
     * 根据用户Id查询模块|菜单（构建前端路由 | 前端产品中心）
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | userId 用户Id | systemId 系统Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectSystemMenuListByUserId(SysSearch search);

    /**
     * 查询系统-菜单集合
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | roleId 角色Id | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> selectSystemMenuList(SysSearch search);

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
