package com.xueyi.system.authority.domain;

import com.xueyi.common.core.web.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统-菜单数组装结构
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

    /** 类型（0系统 1菜单） */
    private String type;

    /** 子部门/岗位 */
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
}
