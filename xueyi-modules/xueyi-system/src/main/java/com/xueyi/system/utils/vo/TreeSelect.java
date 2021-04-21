package com.xueyi.system.utils.vo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.organize.domain.deptPostVo;

/**
 * Treeselect树结构实体类
 *
 * @author xueyi
 * @originalAuthor ruoyi
 */
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点Id
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点状态
     */
    private String status;

    /**
     * 节点类型
     */
    private String type;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.status = dept.getStatus();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.status = menu.getStatus();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(deptPostVo deptPostVo) {
        this.id = deptPostVo.getUid();
        this.label = deptPostVo.getName();
        this.status = deptPostVo.getStatus();
        this.type = deptPostVo.getType();
        this.children = deptPostVo.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
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

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
