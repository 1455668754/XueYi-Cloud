package com.xueyi.tenant.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 数据源对象 xy_tenant_source
 *
 * @author xueyi
 */
public class TenantSource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据源Id */
    private Long sourceId;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String name;

    /** 数据库(0从数据源 1主数据源) */
    @Excel(name = "数据源类型", readConverterExp = "0=从数据源,1=主数据源")
    private String databaseType;

    /** 数据源编码 */
    @Excel(name = "数据源编码")
    private String slave;

    /** 驱动 */
    @Excel(name = "驱动")
    private String driverClassName;

    /** 地址 */
    @Excel(name = "地址")
    private String url;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 主数据源（Y是 N否） */
    @Excel(name = "主数据源", readConverterExp = "Y=是,N=否")
    private String isMain;

    /** 读写类型(0读&写 1只读 2只写) */
    @Excel(name = "读写类型", readConverterExp = "0=读&写,1=只读,2=只写")
    private String type;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 读写分离 读 */
    private List<TenantSourceValue> values;

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public List<TenantSourceValue> getValues() {
        return values;
    }

    public void setValues(List<TenantSourceValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("sourceId", getSourceId())
                .append("name", getName())
                .append("databaseType", getDatabaseType())
                .append("slave", getSlave())
                .append("driverClassName", getDriverClassName())
                .append("url", getUrl())
                .append("username", getUsername())
                .append("password", getPassword())
                .append("values", getValues())
                .append("isMain", getIsMain())
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