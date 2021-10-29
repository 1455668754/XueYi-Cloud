package com.xueyi.common.datasource.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 主数据源配置
 *
 * @author xueyi
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
public class SourceProperties {

    /** 数据源驱动 */
    private String driverClassName;

    /** 数据源路径 */
    private String url;

    /** 数据源账号 */
    private String username;

    /** 数据源密码 */
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
