package com.xueyi.system.api.organize;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户表 xy_tenant
 *
 * @author xueyi
 */
public class SysEnterprise extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 企业账号
     */
    private String enterpriseName;

    /**
     * 系统名称
     */
    private String enterpriseSystemName;

    /**
     * 企业昵称
     */
    private String enterpriseNick;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业账号修改次数
     */
    private Long enterpriseNameFrequency;

    /**
     * 企业访问库(master默认数据库)
     */
    private String datasource;

    /** 归属数据库(0系统默认 1独立库) */
    @Excel(name = "归属数据库(0系统默认 1独立库)")
    private Long attributionDatabase;

    /** 数据源url */
    @Excel(name = "数据源url")
    private String datasourceUrl;

    /** 数据源用户名 */
    @Excel(name = "数据源用户名")
    private String datasourceUsername;

    /** 数据源密码 */
    @Excel(name = "数据源密码")
    private String datasourcePassword;

    /** 数据源驱动 */
    @Excel(name = "数据源驱动")
    private String datasourceDriver;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseSystemName() {
        return enterpriseSystemName;
    }

    public void setEnterpriseSystemName(String enterpriseSystemName) {
        this.enterpriseSystemName = enterpriseSystemName;
    }

    public String getEnterpriseNick() {
        return enterpriseNick;
    }

    public void setEnterpriseNick(String enterpriseNick) {
        this.enterpriseNick = enterpriseNick;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getEnterpriseNameFrequency() {
        return enterpriseNameFrequency;
    }

    public void setEnterpriseNameFrequency(Long enterpriseNameFrequency) {
        this.enterpriseNameFrequency = enterpriseNameFrequency;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public Long getAttributionDatabase() {
        return attributionDatabase;
    }

    public void setAttributionDatabase(Long attributionDatabase) {
        this.attributionDatabase = attributionDatabase;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public void setDatasourceUsername(String datasourceUsername) {
        this.datasourceUsername = datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public String getDatasourceDriver() {
        return datasourceDriver;
    }

    public void setDatasourceDriver(String datasourceDriver) {
        this.datasourceDriver = datasourceDriver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("enterpriseId", getEnterpriseId())
                .append("enterpriseSystemName", getEnterpriseSystemName())
                .append("enterpriseName", getEnterpriseName())
                .append("enterpriseNick", getEnterpriseNick())
                .append("logo", getLogo())
                .append("enterpriseNameFrequency", getEnterpriseNameFrequency())
                .append("datasource", getDatasource())
                .append("datasourceUrl", getDatasourceUrl())
                .append("datasourceUsername", getDatasourceUsername())
                .append("datasourcePassword", getDatasourcePassword())
                .append("datasourceDriver", getDatasourceDriver())
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