package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和部门关联对象 sys_role_dept_post
 *
 * @author xueyi
 */
public class SysRoleDeptPost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 部门Id */
    private Long deptPostId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getDeptPostId() {
        return deptPostId;
    }

    public void setDeptPostId(Long deptPostId) {
        this.deptPostId = deptPostId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("deptPostId", getDeptPostId())
                .toString();
    }
}
