package com.xueyi.system.api.authority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 子系统模块对象 xy_system
 *
 * @author xueyi
 */
public class SysSystem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 系统Id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 跳转类型(0内部跳转 1外部跳转) */
    @Excel(name = "跳转类型(0内部跳转 1外部跳转)")
    private String type;

    /** 跳转路由 */
    @Excel(name = "跳转路由")
    private String route;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setSystemId(Long systemId)
    {
        this.systemId = systemId;
    }

    public Long getSystemId()
    {
        return systemId;
    }

    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public String getSystemName()
    {
        return systemName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("systemId", getSystemId())
                .append("systemName", getSystemName())
                .append("imageUrl", getImageUrl())
                .append("type", getType())
                .append("route", getRoute())
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
