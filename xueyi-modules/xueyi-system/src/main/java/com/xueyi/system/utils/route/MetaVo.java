package com.xueyi.system.utils.route;

/**
 * 路由元信息
 *
 * @author xueyi
 */
public class MetaVo {

    /** 设置该路由在侧边栏和面包屑中展示的名字 */
    private String title;

    /** 设置该路由的图标 */
    private String icon;

    /** 动态路由可打开Tab页数 */
    private int dynamicLevel;

    /** 动态路由的实际Path, 即去除路由的动态部分 */
    private String realPath;

    /** 是否忽略KeepAlive缓存 */
    private Boolean ignoreKeepAlive;

    /** 是否固定标签 */
    private Boolean affix;

    /** 内嵌iframe的地址 */
    private String frameSrc;

    /** 指定该路由切换的动画名 */
    private String transitionName;

    /** 是否隐藏该路由在面包屑上面的显示 */
    private Boolean hideBreadcrumb;

    /** 该路由是否会携带参数，且需要在tab页上面显示 */
    private Boolean carryParam;

    /** 隐藏所有子菜单 */
    private Boolean hideChildrenInMenu;

    /** 是否为单级菜单 */
    private Boolean single;  // 暂无

    /** 当前激活的菜单。用于配置详情页时左侧激活的菜单路径 */
    private String currentActiveMenu;

    /** 当前路由是否在标签页显示 */
    private Boolean hideTab;

    /** 当前路由是否在菜单显示 */
    private Boolean hideMenu;

    /** 是否外链 */
    private Boolean isLink;

    /** 菜单排序，只对第一级有效 */
    private Integer orderNo;

    /** 忽略路由。用于在ROUTE_MAPPING以及BACK权限模式下，生成对应的菜单而忽略路由 */
    private Boolean ignoreRoute;

    /** 是否在子级菜单的完整path中忽略本级path */
    private Boolean hidePathForChildren;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDynamicLevel() {
        return dynamicLevel;
    }

    public void setDynamicLevel(int dynamicLevel) {
        this.dynamicLevel = dynamicLevel;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public Boolean getIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public void setIgnoreKeepAlive(Boolean ignoreKeepAlive) {
        this.ignoreKeepAlive = ignoreKeepAlive;
    }

    public Boolean getAffix() {
        return affix;
    }

    public void setAffix(Boolean affix) {
        this.affix = affix;
    }

    public String getFrameSrc() {
        return frameSrc;
    }

    public void setFrameSrc(String frameSrc) {
        this.frameSrc = frameSrc;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    public Boolean getHideBreadcrumb() {
        return hideBreadcrumb;
    }

    public void setHideBreadcrumb(Boolean hideBreadcrumb) {
        this.hideBreadcrumb = hideBreadcrumb;
    }

    public Boolean getCarryParam() {
        return carryParam;
    }

    public void setCarryParam(Boolean carryParam) {
        this.carryParam = carryParam;
    }

    public Boolean getHideChildrenInMenu() {
        return hideChildrenInMenu;
    }

    public void setHideChildrenInMenu(Boolean hideChildrenInMenu) {
        this.hideChildrenInMenu = hideChildrenInMenu;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public String getCurrentActiveMenu() {
        return currentActiveMenu;
    }

    public void setCurrentActiveMenu(String currentActiveMenu) {
        this.currentActiveMenu = currentActiveMenu;
    }

    public Boolean getHideTab() {
        return hideTab;
    }

    public void setHideTab(Boolean hideTab) {
        this.hideTab = hideTab;
    }

    public Boolean getHideMenu() {
        return hideMenu;
    }

    public void setHideMenu(Boolean hideMenu) {
        this.hideMenu = hideMenu;
    }

    public Boolean getLink() {
        return isLink;
    }

    public void setLink(Boolean link) {
        isLink = link;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getIgnoreRoute() {
        return ignoreRoute;
    }

    public void setIgnoreRoute(Boolean ignoreRoute) {
        this.ignoreRoute = ignoreRoute;
    }

    public Boolean getHidePathForChildren() {
        return hidePathForChildren;
    }

    public void setHidePathForChildren(Boolean hidePathForChildren) {
        this.hidePathForChildren = hidePathForChildren;
    }
}
