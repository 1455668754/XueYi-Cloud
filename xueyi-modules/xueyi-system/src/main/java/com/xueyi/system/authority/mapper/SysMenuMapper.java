package com.xueyi.system.authority.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SystemMenu;

import java.util.List;
import java.util.Set;

/**
 * 菜单表 数据层
 *
 * @author xueyi
 */
public interface SysMenuMapper {

    /**
     * 查询模块-菜单信息列表
     *
     * @param menu 菜单信息 | insideIds 模块-菜单范围集合
     * @return 模块-菜单信息集合
     */
    @DataScope(edAlias = "m")
    public Set<SystemMenu> mainSelectSystemMenuList(SysMenu menu);

    /**
     * 根据菜单Id查询信息
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 菜单信息
     */
    @DataScope(edAlias = "m")
    public SysMenu mainSelectMenuById(SysMenu menu);

    /**
     * 新增菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int mainInsertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @DataScope(uedAlias = "empty")
    public int mainUpdateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(uedAlias = "empty")
    public int mainDeleteMenuById(SysMenu menu);

    /**
     * 校验菜单名称是否唯一
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuName 菜单名称 | parentId 父菜单Id
     * @return 结果
     */
    @DataScope(edAlias = "m")
    public SysMenu mainCheckMenuNameUnique(SysMenu menu);

    /**
     * 检验是否存在菜单子节点
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(edAlias = "m")
    public int mainHasChildByMenuId(SysMenu menu);

















    /**
     * 根据用户Id查询权限（登录校验）
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param menu 菜单信息 | params.roleSystemPerms 菜单Id组（List<SysRoleSystemMenu>） | enterpriseId 租户Id
     * @return 权限列表
     */
    public List<String> checkLoginMenuPermission(SysMenu menu);
}