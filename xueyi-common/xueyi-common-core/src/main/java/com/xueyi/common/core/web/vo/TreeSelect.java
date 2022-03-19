package com.xueyi.common.core.web.vo;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.web.entity.base.TreeEntity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeSelect树结构实体类
 *
 * @author xueyi
 */
public class TreeSelect<T extends TreeEntity<T>> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点Id */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect<T>> children;

    public TreeSelect() {
    }

    public TreeSelect(T t) {
        this.id = t.getId();
        this.label = t.getName();
        this.children = CollUtil.isNotEmpty(t.getChildren()) ? t.getChildren().stream().map(TreeSelect<T>::new).collect(Collectors.toList()) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeSelect<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect<T>> children) {
        this.children = children;
    }
}
