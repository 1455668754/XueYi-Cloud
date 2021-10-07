package com.xueyi.system.cache.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.authority.SystemMenu;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

/**
 * 通用缓存封装对象
 *
 * @author xueyi
 */
public class CacheInitVo extends BaseEntity {

    /** 路由集合 */
    private Set<SysMenu> menuSet;

    /** 模块集合 */
    private Set<SysSystem> systemSet;

    /** 模块-菜单集合 */
    private Set<SystemMenu> systemMenuSet;

    /** 企业集合 */
    private Set<SysEnterprise> enterpriseSet;

    public Set<SysMenu> getMenuSet() {
        return menuSet;
    }

    public void setMenuSet(Set<SysMenu> menuSet) {
        this.menuSet = menuSet;
    }

    public Set<SysSystem> getSystemSet() {
        return systemSet;
    }

    public void setSystemSet(Set<SysSystem> systemSet) {
        this.systemSet = systemSet;
    }

    public Set<SystemMenu> getSystemMenuSet() {
        return systemMenuSet;
    }

    public void setSystemMenuSet(Set<SystemMenu> systemMenuSet) {
        this.systemMenuSet = systemMenuSet;
    }

    public Set<SysEnterprise> getEnterpriseSet() {
        return enterpriseSet;
    }

    public void setEnterpriseSet(Set<SysEnterprise> enterpriseSet) {
        this.enterpriseSet = enterpriseSet;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("enterpriseId", getEnterpriseId())
                .append("routeSet", getMenuSet())
                .append("systemSet", getSystemSet())
                .append("systemMenuSet", getSystemMenuSet())
                .append("enterpriseSet", getEnterpriseSet())
                .toString();
    }
}
