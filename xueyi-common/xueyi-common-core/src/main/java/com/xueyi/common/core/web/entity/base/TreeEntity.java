package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

/**
 * Tree 基类
 *
 * @param <D> Dto
 * @author xueyi
 */
public class TreeEntity<D> extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 父级Id */
    @TableField("parent_id")
    private Long parentId;

    /** 父级名称 */
    @TableField(exist = false)
    private String parentName;

    /** 祖籍列表 */
    @TableField("ancestors")
    private String ancestors;

    /** 子节点集合 */
    @TableField(exist = false)
    private List<D> children;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public List<D> getChildren() {
        return children;
    }

    public void setChildren(List<D> children) {
        this.children = children;
    }
}
