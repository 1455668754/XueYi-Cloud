package com.xueyi.system.api.material;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 素材分类对象 xy_material_folder
 *
 * @author xueyi
 */
public class SysMaterialFolder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类Id
     */
    private Long folderId;

    /**
     * 父类Id
     */
    @Excel(name = "父类Id")
    private Long parentId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String folderName;

    /**
     * 祖级列表
     */
    @Excel(name = "祖级列表")
    private String ancestors;

    /**
     * 分类类型(0默认文件夹 1系统文件夹)
     */
    @Excel(name = "分类类型(0默认文件夹 1系统文件夹)")
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 归属素材对象集合
     */
    private List<Object> materialList;

    /**
     * 子对象集合
     */
    private List<Object> children;

    /** 前端参数 */
    private boolean cssStyle = false;

    /** 前端参数 */
    private boolean hiddenVisible = false;

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public List<Object> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Object> materialList) {
        this.materialList = materialList;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public boolean isCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(boolean cssStyle) {
        this.cssStyle = cssStyle;
    }

    public boolean isHiddenVisible() {
        return hiddenVisible;
    }

    public void setHiddenVisible(boolean hiddenVisible) {
        this.hiddenVisible = hiddenVisible;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("folderId", getFolderId())
                .append("parentId", getParentId())
                .append("folderName", getFolderName())
                .append("materialList", getMaterialList())
                .append("children", getChildren())
                .append("ancestors", getAncestors())
                .append("cssStyle", isCssStyle())
                .append("hiddenVisible", isHiddenVisible())
                .append("type", getType())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
