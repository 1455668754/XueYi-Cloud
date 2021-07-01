package com.xueyi.system.authority.service;

import java.util.List;
import java.util.Set;

import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.utils.vo.RouterVo;
import com.xueyi.system.utils.vo.TreeSelect;

/**
 * 菜单 业务层
 *
 * @author xueyi
 * @originalAuthor ruoyi
 */
public interface ISysMenuService {

//    /**
//     * 根据用户查询系统菜单列表
//     *
//     * @param userId   用户Id
//     * @param systemId 系统Id
//     * @param userType 用户标识
//     * @return 菜单列表
//     */
//    public List<SysMenu> selectMenuList(Long userId, Long systemId, String userType);

//    /**
//     * 根据用户查询系统菜单列表
//     *
//     * @param menus   菜单列表List<SysMenu>
//     * @param systemId 系统Id
//     * @return 菜单列表
//     */
//    public List<SysMenu> selectMenuListExcludeChild(List<SysMenu> menus, Long systemId);

//    /**
//     * 查询系统菜单列表
//     *
//     * @param menu     菜单信息
//     * @param userId   用户Id
//     * @param userType 用户标识
//     * @return 菜单列表
//     */
//    public List<SysMenu> selectMenuListByUserId(SysMenu menu, Long userId, String userType);

//    /**
//     * 根据用户Id查询权限
//     *
//     * @param userId 用户Id
//     * @return 权限列表
//     */
//    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户Id查询菜单
     *
     * @param userId   用户Id
     * @param systemId 系统Id
     * @param userType 用户标识
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId, Long systemId, String userType);

//    /**
//     * 根据角色Id查询菜单树信息
//     *
//     * @param roleId   角色Id
//     * @param systemId 系统Id
//     * @return 选中菜单列表
//     */
//    public List<Integer> selectMenuListByRoleId(Long roleId, Long systemId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

//    /**
//     * 构建前端所需要下拉树结构
//     *
//     * @param menus 菜单列表
//     * @return 下拉树结构列表
//     */
//    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 根据菜单Id查询信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 菜单信息
     */
    public SysMenu selectMenuById(SysMenu menu);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    public int deleteMenuById(SysMenu menu);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息 | menuId   菜单Id | parentId 父级菜单Id | menuName 菜单名称
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 校验是否存在菜单子节点
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(SysMenu menu);

    /**
     * 校验菜单是否存在角色
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(SysMenu menu);
}
