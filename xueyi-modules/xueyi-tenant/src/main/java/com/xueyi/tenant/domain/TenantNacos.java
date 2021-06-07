package com.xueyi.tenant.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;

/**
 * Nacos配置对象 xy_tenant_nacos
 *
 * @author xueyi
 */
public class TenantNacos extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置Id */
    private Long dataId;

    /** 配置名称 */
    @Excel(name = "配置名称")
    private String name;

    /** 头部配置信息 */
    @Excel(name = "头部配置信息")
    private String prefixStr;

    /** 数据源配置信息 */
    @Excel(name = "数据源配置信息")
    private String slaveStr;

    /** 尾部配置信息 */
    @Excel(name = "尾部配置信息")
    private String suffixStr;

    /** 配置类型（0自动配置 1手动配置） */
    @Excel(name = "配置类型", readConverterExp = "0=自动配置,1=手动配置")
    private String type;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrefixStr(String prefixStr) {
        this.prefixStr = prefixStr;
    }

    public String getPrefixStr() {
        return prefixStr;
    }

    public void setSlaveStr(String slaveStr) {
        this.slaveStr = slaveStr;
    }

    public String getSlaveStr() {
        return slaveStr;
    }

    public void setSuffixStr(String suffixStr) {
        this.suffixStr = suffixStr;
    }

    public String getSuffixStr() {
        return suffixStr;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("dataId", getDataId())
                .append("name", getName())
                .append("prefixStr", getPrefixStr())
                .append("slaveStr", getSlaveStr())
                .append("suffixStr", getSuffixStr())
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