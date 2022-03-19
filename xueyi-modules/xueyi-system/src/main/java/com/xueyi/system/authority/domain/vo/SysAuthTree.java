package com.xueyi.system.authority.domain.vo;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


/**
 * 权限对象 通用结构
 *
 * @author xueyi
 */
public class SysAuthTree extends BasisEntity {

    /** Id */
    private Long id;

    /** 父级Id */
    private Long parentId;

    /** 名称 */
    private String label;

    /** 状态 */
    private String status;

    /** 类型（0 模块 1 菜单） */
    private String type;

    /** 子部门/岗位 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysOrganizeTree> children;

    public SysAuthTree() {}

    /**
     * 模块转换
     */
    public SysAuthTree(SysModuleDto module) {
        this.id = module.getId();
        this.parentId = BaseConstants.TOP_ID;
        this.label = module.getName();
        this.status = module.getStatus();
        this.type = AuthorityConstants.AuthorityType.MODULE.getCode();
    }

    /**
     * 菜单转换
     */
    public SysAuthTree(SysMenuDto menu) {
        this.id = menu.getId();
        this.parentId = ObjectUtil.equals(menu.getParentId(), AuthorityConstants.MENU_TOP_NODE) ? menu.getModuleId(): menu.getParentId();
        this.label = menu.getTitle();
        this.status = menu.getStatus();
        this.type = AuthorityConstants.AuthorityType.MENU.getCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public List<SysOrganizeTree> getChildren() {
        return children;
    }

    public void setChildren(List<SysOrganizeTree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("label", getLabel())
                .append("status", getStatus())
                .append("type", getType())
                .append("children", getChildren())
                .toString();
    }
}
