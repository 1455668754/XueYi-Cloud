package com.xueyi.system.api.domain.material;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 素材表 xy_material
 *
 * @author xueyi
 */
public class SysMaterial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 素材Id */
    private Long materialId;

    /** 分类Id */
    @Excel(name = "分类Id")
    private Long folderId;

    /** 素材昵称 */
    @Excel(name = "素材昵称")
    private String materialNick;

    /** 素材名称 */
    @Excel(name = "素材名称")
    private String materialName;

    /** 素材名称 */
    @Excel(name = "原图名称")
    private String materialOriginalName;

    /** 素材地址 */
    @Excel(name = "素材地址")
    private String materialUrl;

    /** 原图地址 */
    @Excel(name = "原图地址")
    private String materialOriginalUrl;

    /** 素材大小 */
    @Excel(name = "素材大小")
    private BigDecimal materialSize;

    /** 素材类型(0默认素材 1系统素材) */
    @Excel(name = "素材类型(0默认素材 1系统素材)")
    private String type;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 前端参数 */
    private boolean imageChoice = false;

    /** 前端参数 */
    private boolean hiddenVisible = false;

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setFolderId(Long folderId)
    {
        this.folderId = folderId;
    }

    public Long getFolderId()
    {
        return folderId;
    }
    public void setMaterialNick(String materialNick)
    {
        this.materialNick = materialNick;
    }

    public String getMaterialNick()
    {
        return materialNick;
    }
    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public String getMaterialName()
    {
        return materialName;
    }
    public void setMaterialOriginalName(String materialOriginalName) {
        this.materialOriginalName = materialOriginalName;
    }

    public String getMaterialOriginalName() {
        return materialOriginalName;
    }
    public void setMaterialUrl(String materialUrl)
    {
        this.materialUrl = materialUrl;
    }

    public String getMaterialUrl()
    {
        return materialUrl;
    }
    public void setMaterialOriginalUrl(String materialOriginalUrl) {
        this.materialOriginalUrl = materialOriginalUrl;
    }

    public String getMaterialOriginalUrl() {
        return materialOriginalUrl;
    }
    public void setMaterialSize(BigDecimal materialSize)
    {
        this.materialSize = materialSize;
    }

    public BigDecimal getMaterialSize()
    {
        return materialSize;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public boolean isImageChoice() {
        return imageChoice;
    }

    public void setImageChoice(boolean imageChoice) {
        this.imageChoice = imageChoice;
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
                .append("materialId", getMaterialId())
                .append("folderId", getFolderId())
                .append("materialNick", getMaterialNick())
                .append("materialName", getMaterialName())
                .append("materialOriginalName", getMaterialOriginalName())
                .append("materialUrl", getMaterialUrl())
                .append("materialOriginalUrl", getMaterialOriginalUrl())
                .append("materialSize", getMaterialSize())
                .append("imageChoice", isImageChoice())
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
