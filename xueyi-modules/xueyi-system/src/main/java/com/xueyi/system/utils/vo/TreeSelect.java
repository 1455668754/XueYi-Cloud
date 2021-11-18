package com.xueyi.system.utils.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.domain.authority.SystemMenu;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.organize.domain.deptPostVo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeSelect树结构实体类
 *
 * @author xueyi
 */
public class TreeSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点Id */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 节点状态 */
    private String status;

    /** 节点类型 */
    private String type;

    /** 显示顺序 */
    private Integer sort;

    /** 权限字符串 | 模块&&菜单专属 */
    private String perms;

    /** 菜单图标 | 模块&&菜单专属 */
    private String icon;

    /** 组件路径 | 模块&&菜单专属 */
    private String component;

    /** 系统Id | 模块&&菜单专属 */
    private Long systemId;

    /** 公共模块-菜单（Y是 N否） | 模块&&菜单专属 */
    private String isCommon;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.status = dept.getStatus();
        this.children = dept.getChildren() != null ? dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList()) : null;
    }

    public TreeSelect(deptPostVo deptPostVo) {
        this.id = deptPostVo.getUid();
        this.label = deptPostVo.getName();
        this.status = (deptPostVo.getStatus().equals("1") || (StringUtils.isEmpty(deptPostVo.getChildren())) && deptPostVo.getType().equals("0")) ? "1" : "0";
        this.type = deptPostVo.getType();
        this.children = deptPostVo.getChildren() != null ? deptPostVo.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList()) : null;
    }

    public TreeSelect(SystemMenu systemMenu) {
        this.id = systemMenu.getUid();
        this.label = systemMenu.getName();
        this.type = systemMenu.getType();
        this.sort = systemMenu.getSort();
        this.status = systemMenu.getStatus();
        this.perms = systemMenu.getPerms();
        this.icon = systemMenu.getIcon();
        this.component = systemMenu.getComponent();
        this.isCommon = systemMenu.getIsCommon();
        this.systemId = systemMenu.getSystemId();
        this.children = systemMenu.getChildren() != null ? systemMenu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList()) : null;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
