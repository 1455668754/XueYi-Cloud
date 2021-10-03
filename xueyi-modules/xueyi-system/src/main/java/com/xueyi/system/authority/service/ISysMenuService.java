package com.xueyi.system.authority.service;

import java.util.List;

import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.utils.vo.RouterVo;
import com.xueyi.system.utils.vo.TreeSelect;

/**
 * 菜单 业务层
 *
 * @author xueyi
 */
public interface ISysMenuService {

    /**
     * 根据用户Id查询菜单路由
     *
     * @param menu 菜单信息 | systemId 系统Id
     * @return 菜单列表
     */
    public List<SysMenu> getRoutes(SysMenu menu);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 查询模块-菜单信息列表
     *
     * @param menu 菜单信息
     * @return 模块-菜单信息集合
     */
    public List<TreeSelect> mainSelectSystemMenuList(SysMenu menu);

    /**
     * 根据菜单Id查询信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 菜单信息
     */
    public SysMenu mainSelectMenuById(SysMenu menu);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int mainInsertMenu(SysMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int mainUpdateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    public int mainDeleteMenuById(SysMenu menu);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息 | menuId   菜单Id | parentId 父级菜单Id | menuName 菜单名称
     * @return 结果
     */
    public boolean mainCheckMenuNameUnique(SysMenu menu);

    /**
     * 校验是否存在菜单子节点
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean mainHasChildByMenuId(SysMenu menu);

    /**
     * 校验菜单是否存在角色
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean mainCheckMenuExistRole(SysMenu menu);
}
