package com.xueyi.common.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 租户表控制配置
 *
 * @author xueyi
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "xueyi.tenant")
public class TenantProperties {

    /** 公共表 */
    private String[] commonTable = {};

    /** 非租户表 */
    private String[] excludeTable = {};

    public String[] getCommonTable() {
        return commonTable;
    }

    public void setCommonTable(String[] commonTable) {
        this.commonTable = commonTable;
    }

    public String[] getExcludeTable() {
        return excludeTable;
    }

    public void setExcludeTable(String[] excludeTable) {
        this.excludeTable = excludeTable;
    }
}
