package com.xueyi.system.api.domain.authority;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块-菜单数组装结构
 *
 * @author xueyi
 */
public class SystemMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** Id */
    private Long Uid;

    /** 父Id（系统父Id为-1） */
    private Long FUid;

    /** 名称 */
    private String name;

    /** 类型（0模块 1菜单） */
    private String type;

    /** 组件路径 */
    private String component;

    /** 菜单状态（0显示 1隐藏） */
    private String status;

    /** 权限字符串 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 公共模块-菜单（Y是 N否） */
    private String isCommon;

    /** 子模块-菜单 */
    private List<SystemMenu> children = new ArrayList<SystemMenu>();

    public Long getUid() {
        return Uid;
    }

    public void setUid(Long uid) {
        Uid = uid;
    }

    public Long getFUid() {
        return FUid;
    }

    public void setFUid(Long FUid) {
        this.FUid = FUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public List<SystemMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SystemMenu> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        return Uid != 0L ? Uid.hashCode() : 0;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof SystemMenu) {
            SystemMenu menuVo = (SystemMenu) anObject;
            return this.Uid.equals(menuVo.getUid());
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Uid", getUid())
                .append("FUid", getFUid())
                .append("name", getName())
                .append("type", getType())
                .append("component", getComponent())
                .append("perms", getPerms())
                .append("icon", getIcon())
                .append("status", getStatus())
                .append("isCommon", getIsCommon())
                .append("systemId", getSystemId())
                .append("children", getChildren())
                .toString();
    }
}