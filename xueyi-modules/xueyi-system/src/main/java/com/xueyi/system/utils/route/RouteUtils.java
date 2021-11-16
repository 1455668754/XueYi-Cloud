package com.xueyi.system.utils.route;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.HttpConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.domain.authority.SysMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 路由树工具类 - vue2
 *
 * @author xueyi
 */
public class RouteUtils {

    /**
     * 构建前端路由所需要的菜单 - vue2
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public static List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(StringUtils.equals(AuthorityConstants.Visible.NO.getCode(), menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon(), StringUtils.equals(AuthorityConstants.Cache.NO.getCode(), menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (cMenus != null && !cMenus.isEmpty() && cMenus.size() > 0 && StringUtils.equals(AuthorityConstants.MenuType.DIR.getCode(), menu.getMenuType())) {
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
                children.setMeta(new MetaVo(menu.getName(), menu.getIcon(), StringUtils.equals(AuthorityConstants.Cache.NO.getCode(), menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == AuthorityConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = StringUtils.replaceEach(menu.getPath(), new String[]{HttpConstants.Type.HTTP.getCode(), HttpConstants.Type.HTTPS.getCode()}, new String[]{"", ""});
                children.setPath(routerPath);
                children.setComponent(AuthorityConstants.ComponentType.INNER_LINK.getCode());
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
    public static String getRouteName(SysMenu menu) {
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
    public static String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != AuthorityConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
            routerPath = StringUtils.replaceEach(routerPath, new String[]{HttpConstants.Type.HTTP.getCode(), HttpConstants.Type.HTTPS.getCode()}, new String[]{"", ""});
        }
        // 非外链并且是一级目录（类型为目录）
        if (AuthorityConstants.MENU_TOP_NODE == menu.getParentId().intValue() && StringUtils.equals(AuthorityConstants.MenuType.DIR.getCode(), menu.getMenuType())
                && StringUtils.equals(AuthorityConstants.Frame.NO.getCode(), menu.getIsFrame())) {
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
    public static String getComponent(SysMenu menu) {
        String component = AuthorityConstants.ComponentType.LAYOUT.getCode();
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != AuthorityConstants.MENU_TOP_NODE.intValue() && isInnerLink(menu)) {
            component = AuthorityConstants.ComponentType.INNER_LINK.getCode();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = AuthorityConstants.ComponentType.PARENT_VIEW.getCode();
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && StringUtils.equals(AuthorityConstants.MenuType.MENU.getCode(), menu.getMenuType())
                && StringUtils.equals(AuthorityConstants.Frame.NO.getCode(), menu.getIsFrame());
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isInnerLink(SysMenu menu) {
        return StringUtils.equals(AuthorityConstants.Frame.NO.getCode(), menu.getIsFrame()) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && StringUtils.equals(AuthorityConstants.MenuType.DIR.getCode(), menu.getMenuType());
    }
}
