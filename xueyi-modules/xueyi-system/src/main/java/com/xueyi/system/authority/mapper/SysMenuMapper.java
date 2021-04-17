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
     * 查询系统菜单列表
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @DataScope(systemAlias = "m")
    public List<SysMenu> selectMenuList(SysMenu menu);


    /**
     * 根据用户查询系统菜单列表
     * 访问控制 m 租户查询
     *
     * @param menu 菜单信息 | params中携带userId
     * @return 菜单列表
     */
    @DataScope(systemAlias = "m")
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据用户Id查询权限
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | userId 用户Id
     * @return 权限列表
     */
    @DataScope(systemAlias = "m")
    public List<String> selectMenuPermsByUserId(SysSearch search);

    /**
     * 根据用户Id查询菜单
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | systemId 系统Id
     * @return 菜单列表
     */
    @DataScope(systemAlias = "m")
    public List<SysMenu> selectMenuTreeAll(SysSearch search);

    /**
     * 根据用户Id查询菜单
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | username 用户Id | systemId 系统Id
     * @return 菜单列表
     */
    @DataScope(systemAlias = "m")
    public List<SysMenu> selectMenuTreeByUserId(SysSearch search);

    /**
     * 根据角色Id查询菜单树信息
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | roleId 角色Id | menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    @DataScope(systemAlias = "m")
    public List<Integer> selectMenuListByRoleId(SysSearch search);

    /**
     * 根据菜单Id查询信息
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | menuId 菜单Id
     * @return 菜单信息
     */
    @DataScope(systemAlias = "m")
    public SysMenu selectMenuById(SysSearch search);

    /**
     * 新增菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteMenuById(SysSearch search);

    /**
     * 校验菜单名称是否唯一
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | menuName 菜单名称 | parentId 父菜单Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "m")
    public SysMenu checkMenuNameUnique(SysSearch search);

    /**
     * 检验是否存在菜单子节点
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(systemAlias = "m")
    public int hasChildByMenuId(SysSearch search);
}

