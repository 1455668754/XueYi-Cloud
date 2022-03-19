package com.xueyi.system.utils.route;

/**
 * 路由元信息
 *
 * @author xueyi
 */
public class MetaVo {

    /** 设置该路由在侧边栏和面包屑中展示的名字 */
    private String title;

    /** 设置该路由的图标，对应路径common/src/assets/icons/svg */
    private String icon;

    /** 设置为true，则不会被 <keep-alive>缓存 */
    private boolean noCache;

    /** 内链地址（http(s)://开头） */
    private String link;

    /** 是否隐藏该路由在面包屑上面的显示 */
    private Boolean breadcrumb;

    /** 是否固定标签 */
    private Boolean affix;

    /** 当前激活的菜单。用于配置详情页时左侧激活的菜单路径 */
    private String activeMenu;

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

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(Boolean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public Boolean getAffix() {
        return affix;
    }

    public void setAffix(Boolean affix) {
        this.affix = affix;
    }

    public String getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }
}
