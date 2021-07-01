package com.xueyi.system.authority.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.domain.SysMenu;

/**
 * 菜单表 数据层
 *
 * @author xueyi
 */
public interface SysMenuMapper {
    /**
     * 根据用户Id查询权限（登录校验）
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param search 万用组件 | userId 用户Id | enterpriseId 租户Id
     * @return 权限列表
     */
    public List<String> checkLoginMenuPermission(SysSearch search);

    /**
     * 查询系统全部菜单列表
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | status 菜单状态
     * @return 菜单列表
     */
    @DataScope(edAlias = "m")
    public List<SysMenu> selectMenuListAll(SysMenu menu);

    /**
     * 根据用户Id查询菜单
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | systemId 系统Id
     * @return 菜单列表
     */
    @DataScope(SedAlias = "m")
    public List<SysMenu> selectMenuTreeAll(SysMenu menu);

    /**
     * 根据用户Id查询菜单
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | params.userId 用户Id | systemId 系统Id
     * @return 菜单列表
     */
    @DataScope(SedAlias = "m")
    public List<SysMenu> selectMenuTreeByUserId(SysMenu menu);

    /**
     * 根据菜单Id查询信息
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 菜单信息
     */
    @DataScope(edAlias = "m")
    public SysMenu selectMenuById(SysMenu menu);

    /**
     * 新增菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteMenuById(SysMenu menu);

    /**
     * 校验菜单名称是否唯一
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuName 菜单名称 | parentId 父菜单Id
     * @return 结果
     */
    @DataScope(eAlias = "m")
    public SysMenu checkMenuNameUnique(SysMenu menu);

    /**
     * 检验是否存在菜单子节点
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(edAlias = "m")
    public int hasChildByMenuId(SysMenu menu);
}