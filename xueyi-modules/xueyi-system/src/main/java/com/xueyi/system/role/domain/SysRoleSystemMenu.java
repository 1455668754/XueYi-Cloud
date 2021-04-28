package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和系统关联对象 sys_role_system_menu
 *
 * @author xueyi
 */
public class SysRoleSystemMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 系统Id */
    private Long systemMenuId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getSystemMenuId() {
        return systemMenuId;
    }

    public void setSystemMenuId(Long systemMenuId) {
        this.systemMenuId = systemMenuId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("systemMenuId", getSystemMenuId())
                .toString();
    }
}
