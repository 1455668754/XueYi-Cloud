package com.xueyi.system.authority.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.api.utilTool.SysSearch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.utils.vo.MetaVo;
import com.xueyi.system.utils.vo.RouterVo;
import com.xueyi.system.utils.vo.TreeSelect;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.role.mapper.SysRoleMenuMapper;
import com.xueyi.system.authority.service.ISysMenuService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId   用户Id
     * @param systemId 系统Id
     * @param userType 用户标识
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId, Long systemId, String userType) {
        SysMenu menu = new SysMenu();
        menu.setSystemId(systemId);
        return selectMenuListByUserId(menu, userId, userType);
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menus  菜单列表List<SysMenu>
     * @param menuId 菜单Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuListExcludeChild(List<SysMenu> menus, Long menuId) {
        List<SysMenu> childList = getChildPerms(menus, menuId);
        List<Long> list = new ArrayList<Long>();
        for (SysMenu menu : childList) {
            list.add(menu.getMenuId());
        }
        Iterator<SysMenu> it = menus.iterator();
        while (it.hasNext()) {
            SysMenu m = (SysMenu) it.next();
            if (m.getMenuId().longValue() == menuId.longValue() || ArrayUtils.contains(list.toArray(), m.getMenuId())) {
                it.remove();
            }
        }
        return menus;
    }


    /**
     * 根据用户Id查询权限
     *
     * @param userId 用户Id
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        List<String> perms = menuMapper.selectMenuPermsByUserId(search);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户Id查询菜单
     *
     * @param userId   用户Id
     * @param systemId 系统Id
     * @param userType 用户标识
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId, Long systemId, String userType) {
        List<SysMenu> menus;
        SysSearch search = new SysSearch();
        // 管理员显示所有菜单信息
        search.getSearch().put("systemId", systemId);
        if (SysUser.isAdmin(userType)) {
            menus = menuMapper.selectMenuTreeAll(search);
        } else {
            search.getSearch().put("userId", userId);
            menus = menuMapper.selectMenuTreeByUserId(search);
        }
        return getChildPerms(menus, 0L);
    }

    /**
     * 根据角色Id查询菜单树信息
     *
     * @param roleId   角色Id
     * @param systemId 系统Id
     * @return 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId, Long systemId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", roleId);
        search.getSearch().put("systemId", systemId);
        SysRole role = roleMapper.selectRoleById(search);
        search.getSearch().put("roleId", roleId);
        search.getSearch().put("menuCheckStrictly", role.isMenuCheckStrictly());
        return menuMapper.selectMenuListByRoleId(search);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu     菜单信息
     * @param userId   用户Id
     * @param userType 用户标识
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuListByUserId(SysMenu menu, Long userId, String userType) {
        List<SysMenu> menus;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userType)) {
            menus = menuMapper.selectMenuList(menu);//@param menu 菜单信息
        } else {
            menu.getParams().put("userId", userId);
            menus = menuMapper.selectMenuListByUserId(menu);//@param menu 菜单信息 | params中携带userId
        }
        return menus;
    }

    /**
     * 根据菜单Id查询信息
     *
     * @param menuId 菜单Id
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("menuId", menuId);
        return menuMapper.selectMenuById(search);//@param search 万用组件 | menuId 菜单Id
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);//@param menu 菜单信息
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);//@param menu 菜单信息
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMenuById(Long menuId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("menuId", menuId);
        return menuMapper.deleteMenuById(search);//@param search 万用组件 | menuId 菜单Id
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuId   菜单Id
     * @param parentId 父级菜单Id
     * @param menuName 菜单名称
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(Long menuId, Long parentId, String menuName) {
        if (StringUtils.isNull(menuId)) {
            menuId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("menuName", menuName);
        search.getSearch().put("parentId", parentId);
        SysMenu info = menuMapper.checkMenuNameUnique(search);//@param search 万用组件 | menuName 菜单名称 | parentId 父菜单Id
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验是否存在菜单子节点
     *
     * @param menuId 菜单Id
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("menuId", menuId);
        int result = menuMapper.hasChildByMenuId(search);//@param search 万用组件 | menuId 菜单Id
        return result > 0;
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param menuId 菜单Id
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("menuId", menuId);
        int result = roleMenuMapper.checkMenuExistRole(search);//@param search 万用组件 | menuId 菜单Id
        return result > 0;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的Id获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点Id
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    菜单信息
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}
