package com.xueyi.system.authority.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统-菜单数组装结构
 *
 * @author xueyi
 */
public class SystemMenuVo  extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Id */
    private Long Uid;

    /** 父Id（系统父Id为-1） */
    private Long FUid;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

    /** 类型（0系统 1菜单） */
    private String type;

    /** 权限字符串 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 组件路径 */
    private String component;

    /** 系统默认（0非默认 1默认） */
    private String isMain;

    /** 子部门/岗位 */
    private List<SystemMenuVo> children = new ArrayList<SystemMenuVo>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public List<SystemMenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<SystemMenuVo> children) {
        this.children = children;
    }
}
