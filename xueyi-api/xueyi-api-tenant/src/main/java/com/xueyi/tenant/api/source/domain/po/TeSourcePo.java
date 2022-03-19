package com.xueyi.tenant.api.source.domain.po;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;

/**
 * 数据源 持久化对象
 *
 * @author xueyi
 */
public class TeSourcePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 数据源编码 */
    @Excel(name = "数据源编码")
    @TableField("slave")
    private String slave;

    /** 驱动 */
    @Excel(name = "驱动")
    @TableField("driver_class_name")
    private String driverClassName;

    /** 连接地址 */
    @Excel(name = "连接地址")
    @TableField("url_prepend")
    private String urlPrepend;

    /** 连接参数 */
    @Excel(name = "连接参数")
    @TableField("url_append")
    private String urlAppend;

    /** 用户名 */
    @Excel(name = "用户名")
    @TableField("username")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    @TableField("password")
    private String password;

    /** 默认数据源（Y是 N否） */
    @Excel(name = "默认数据源", readConverterExp = "Y=是,N=否")
    @TableField("is_default")
    private String isDefault;

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public String getSlave() {
        return slave;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setUrlPrepend(String urlPrepend) {
        this.urlPrepend = urlPrepend;
    }

    public String getUrlPrepend() {
        return urlPrepend;
    }

    public void setUrlAppend(String urlAppend) {
        this.urlAppend = urlAppend;
    }

    public String getUrlAppend() {
        return urlAppend;
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

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefault() {
        return isDefault;
    }

}