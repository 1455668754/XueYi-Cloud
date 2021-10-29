package com.xueyi.common.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 演示模式配置
 *
 * @author xueyi
 */
@Component
@ConfigurationProperties(prefix = "xueyi.demo")
public class DemoProperties {

    /** 演示模式开关 */
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}