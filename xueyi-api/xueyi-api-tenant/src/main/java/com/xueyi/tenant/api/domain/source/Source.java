package com.xueyi.tenant.api.domain.source;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 数据源对象 xy_tenant_source
 *
 * @author xueyi
 */
public class Source extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 数据源Id */
    private Long sourceId;

    /** 数据源名称 */
    private String name;

    /** 数据库(0普通数据源 1默认数据源) */
    private String databaseType;

    /** 数据源编码 */
    private String slave;

    /** 驱动 */
    private String driverClassName;

    /** 地址 */
    private String url;

    /** 连接地址 */
    private String urlPrepend;

    /** 连接参数 */
    private String urlAppend;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 主数据源（Y是 N否） */
    private String isMain;

    /** 读写类型(0读&写 1只读 2只写) */
    private String type;

    /** 状态（0正常 1停用） */
    private String status;

    /** 读写分离 读 */
    private List<Source> values;

    /** 源同步策略（0不变 1刷新 2启动 3删除） */
    private int syncType;

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

    public String getUrlPrepend() {
        return urlPrepend;
    }

    public void setUrlPrepend(String urlPrepend) {
        this.urlPrepend = urlPrepend;
    }

    public String getUrlAppend() {
        return urlAppend;
    }

    public void setUrlAppend(String urlAppend) {
        this.urlAppend = urlAppend;
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

    public List<Source> getValues() {
        return values;
    }

    public void setValues(List<Source> values) {
        this.values = values;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
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
                .append("urlPrepend", getUrlPrepend())
                .append("urlAppend", getUrlAppend())
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