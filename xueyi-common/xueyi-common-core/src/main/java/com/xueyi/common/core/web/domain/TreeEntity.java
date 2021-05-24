package com.xueyi.common.core.web.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 * 
 * @author xueyi
 */
public class TreeEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 父菜单Id */
    private Long parentId;

    /** 父菜单名称 */
    private String parentName;

    /** 祖级列表 */
    private String ancestors;

    /** 子部门 */
    private List<?> children = new ArrayList<>();

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<?> getChildren()
    {
        return children;
    }

    public void setChildren(List<?> children)
    {
        this.children = children;
    }
}
