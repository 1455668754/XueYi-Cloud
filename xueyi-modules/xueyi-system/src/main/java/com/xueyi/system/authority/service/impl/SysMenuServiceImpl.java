package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.SortUtils;
import com.xueyi.common.core.utils.multiTenancy.TreeBuildUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SystemMenu;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import com.xueyi.system.utils.vo.MetaVo;
import com.xueyi.system.utils.vo.RouterVo;
import com.xueyi.system.utils.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private ISysAuthorityService authorityService;

    /**
     * 根据用户Id查询菜单路由
     *
     * @param menu 菜单信息 | systemId 系统Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> getRoutes(SysMenu menu) {
        Set<SysMenu> menuSet = AuthorityUtils.getRouteCache(SecurityUtils.getEnterpriseId(), menu.getSystemId());
        Set<SysMenu> rangeSet;
        // 管理员显示所有菜单信息
        if (SecurityUtils.isAdminUser()) {
            rangeSet = authorityService.selectMenuSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false, true);
        } else {
            SysRole role = new SysRole();
            role.getParams().put("userId", SecurityUtils.getUserId());
            rangeSet = authorityService.selectMenuSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByUserId(role), SecurityUtils.isAdminTenant(), true, true);
        }
        menuSet.retainAll(rangeSet);
        return getChildPerms(SortUtils.sortSetToList(menuSet), MenuConstants.MENU_TOP_NODE);
    }

    /**
     * 查询模块-菜单信息列表
     *
     * @param menu 菜单信息
     * @return 模块-菜单信息集合
     */
    @Override
    public List<TreeSelect> mainSelectSystemMenuList(SysMenu menu) {
        Set<SystemMenu> menuSet = new HashSet<>();
        Map<String, Set<SystemMenu>> map;
        if (SecurityUtils.isAdminUser()) {
            map = authorityService.assembleSystemMenuSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false, true);
        } else {
            SysRole role = new SysRole();
            role.getParams().put("userId", SecurityUtils.getUserId());
            map = authorityService.assembleSystemMenuSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByUserId(role), SecurityUtils.isAdminTenant(), true, true);
        }
        menuSet.addAll(map.get("halfIds"));
        menuSet.addAll(map.get("wholeIds"));
        menu.getParams().put("insideIds", menuSet);
        List<SystemMenu> systemMenus = TreeBuildUtils.buildSystemMenuTree(SortUtils.sortSetToList(menuMapper.mainSelectSystemMenuList(menu)), "Uid", "FUid", "children", MenuConstants.SYSTEM_TOP_NODE, false);
        return systemMenus.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单Id查询信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 菜单信息
     */
    @Override
    public SysMenu mainSelectMenuById(SysMenu menu) {
        return menuMapper.mainSelectMenuById(menu);
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainInsertMenu(SysMenu menu) {
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, menu.getIsCommon()) && SecurityUtils.isAdminTenant()) {
            menu.setEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
        }
        return menuMapper.mainInsertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainUpdateMenu(SysMenu menu) {
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, menu.getIsCommon()) && SecurityUtils.isAdminTenant()) {
            menu.setEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
        }
        return menuMapper.mainUpdateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @Override
    public int mainDeleteMenuById(SysMenu menu) {
        return menuMapper.mainDeleteMenuById(menu);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息 | menuId 菜单Id | parentId 父级菜单Id | menuName 菜单名称
     * @return 结果
     */
    @Override
    public boolean mainCheckMenuNameUnique(SysMenu menu) {
        if (StringUtils.isNull(menu.getMenuId())) {
            menu.setMenuId(-1L);
        }
        SysMenu info = menuMapper.mainCheckMenuNameUnique(menu);
        return !StringUtils.isNotNull(info) || info.getMenuId().longValue() == menu.getMenuId().longValue();
    }

    /**
     * 校验是否存在菜单子节点
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @Override
    public boolean mainHasChildByMenuId(SysMenu menu) {
        int result = menuMapper.mainHasChildByMenuId(menu);
        return result > 0;
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param menu 菜单信息 | menuId 菜单Id
     * @return 结果
     */
    @Override
    @DS("#isolate")
    public boolean mainCheckMenuExistRole(SysMenu menu) {
        SysSearch search = new SysSearch();
        search.getSearch().put("systemMenuId", menu.getMenuId());
        int result = roleSystemMenuMapper.checkSystemMenuExistRole(search);//@param search 万用组件 | systemMenuId 系统-菜单Id
        return result > 0;
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
            router.setHidden(MenuConstants.VISIBLE_FALSE.equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon(), StringUtils.equals(MenuConstants.NO_CACHE, menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && MenuConstants.TYPE_DIR.equals(menu.getMenuType())) {
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
                children.setMeta(new MetaVo(menu.getName(), menu.getIcon(), StringUtils.equals(MenuConstants.NO_CACHE, menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == MenuConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = StringUtils.replaceEach(menu.getPath(), new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
                children.setPath(routerPath);
                children.setComponent(MenuConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
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
        // 内链打开外网方式
        if (menu.getParentId().intValue() != MenuConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
            routerPath = StringUtils.replaceEach(routerPath, new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
        }
        // 非外链并且是一级目录（类型为目录）
        if (MenuConstants.MENU_TOP_NODE == menu.getParentId().intValue() && MenuConstants.TYPE_DIR.equals(menu.getMenuType())
                && MenuConstants.NO_FRAME.equals(menu.getIsFrame())) {
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
        String component = MenuConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != MenuConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
            component = MenuConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = MenuConstants.PARENT_VIEW;
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
        return menu.getParentId().intValue() == 0 && MenuConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(MenuConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(MenuConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && MenuConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的Id获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点Id
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点Id,遍历该父节点的所有子节点
            if (t.getParentId().longValue() == parentId.longValue()) {
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
        List<SysMenu> tList = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tList.add(n);
            }
        }
        return tList;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}