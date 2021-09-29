package com.xueyi.system.cache.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.api.domain.authority.SysSystem;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

/**
 * 通用缓存封装工具类
 *
 * @author xueyi
 */
public class CacheInitVo extends BaseEntity {

    /** 菜单集合 */
    private Set<SysMenu> menuSet;

    /** 模块集合 */
    private Set<SysSystem> systemSet;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("enterpriseId", getEnterpriseId())
                .append("menuSet", getMenuSet())
                .append("systemSet", getSystemSet())
                .toString();
    }
}
