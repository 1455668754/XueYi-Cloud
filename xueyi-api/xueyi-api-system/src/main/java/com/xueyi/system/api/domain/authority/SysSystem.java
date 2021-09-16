package com.xueyi.system.api.domain.authority;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 子系统模块对象 xy_system
 *
 * @author xueyi
 */
public class SysSystem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String name;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 跳转类型(0内部跳转 1外部跳转) */
    @Excel(name = "跳转类型(0内部跳转 1外部跳转)")
    private String type;

    /** 跳转新页(Y是 N否) */
    private String isNew;

    /** 跳转路由 */
    @Excel(name = "跳转路由")
    private String route;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 系统默认（0非默认 1默认） */
    private String isMain;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public void setRoute(String route)
    {
        this.route = route;
    }

    public String getRoute()
    {
        return route;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("systemId", getSystemId())
                .append("name", getName())
                .append("imageUrl", getImageUrl())
                .append("type", getType())
                .append("route", getRoute())
                .append("isMain", getIsMain())
                .append("isNew", getIsNew())
                .append("isChange", getIsChange())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
