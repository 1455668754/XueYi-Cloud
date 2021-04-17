package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和系统关联对象 sys_role_system
 *
 * @author xueyi
 */
public class SysRoleSystem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 系统Id */
    private Long systemId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSystemId() {
        return systemId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("systemId", getSystemId())
                .toString();
    }
}
