package com.xueyi.system.utils.vo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.authority.domain.SystemMenuVo;
import com.xueyi.system.organize.domain.deptPostVo;

/**
 * Treeselect树结构实体类
 *
 * @author xueyi
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

    /** 权限字符串 | 模块&&菜单专属 */
    private String perms;

    /** 菜单图标 | 模块&&菜单专属 */
    private String icon;

    /** 组件路径 | 模块&&菜单专属 */
    private String component;

    /** 系统Id | 模块&&菜单专属 */
    private Long systemId;

    /** 系统默认（0非默认 1默认） | 模块&&菜单专属 */
    private String isMain;

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

    public TreeSelect(deptPostVo deptPostVo) {
        this.id = deptPostVo.getUid();
        this.label = deptPostVo.getName();
        this.status = (deptPostVo.getStatus().equals("1") || (StringUtils.isEmpty(deptPostVo.getChildren())) && deptPostVo.getType().equals("0"))?"1":"0";
        this.type = deptPostVo.getType();
        this.children = deptPostVo.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SystemMenuVo systemMenuVo) {
        this.id = systemMenuVo.getUid();
        this.label = systemMenuVo.getName();
        this.status = systemMenuVo.getStatus();
        this.type = systemMenuVo.getType();
        this.perms = systemMenuVo.getPerms();
        this.icon = systemMenuVo.getIcon();
        this.component = systemMenuVo.getComponent();
        this.systemId = systemMenuVo.getSystemId();
        this.isMain = systemMenuVo.getIsCommon();
        this.children = systemMenuVo.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
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

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
