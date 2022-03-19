package com.xueyi.system.utils.route;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author xueyi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {

    /** 路由名字 */
    private String name;

    /** 路由地址 */
    private String path;

    /** 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击 */
    private String redirect;  // 暂无

    /** 组件地址 */
    private String component;

    /** 是否禁用 */
    private Boolean disabled;

    /** 菜单标签设置 */
    private TagVo tag;

    /** 路由参数：如 {"id": 1, "name": "xy"} */
    private String paramPath;

    /** 其他元素 */
    private MetaVo meta;

    /** 子路由 */
    private List<RouterVo> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public TagVo getTag() {
        return tag;
    }

    public void setTag(TagVo tag) {
        this.tag = tag;
    }

    public String getParamPath() {
        return paramPath;
    }

    public void setParamPath(String paramPath) {
        this.paramPath = paramPath;
    }

    public MetaVo getMeta() {
        return meta;
    }

    public void setMeta(MetaVo meta) {
        this.meta = meta;
    }

    public List<RouterVo> getChildren() {
        return children;
    }

    public void setChildren(List<RouterVo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "RouterVo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", redirect='" + redirect + '\'' +
                ", component='" + component + '\'' +
                ", disabled=" + disabled +
                ", tag=" + tag +
                ", paramPath='" + paramPath + '\'' +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }
}
