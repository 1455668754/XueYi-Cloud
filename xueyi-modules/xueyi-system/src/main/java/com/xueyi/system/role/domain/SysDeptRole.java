package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 部门和角色关联对象 sys_dept_role
 *
 * @author xueyi
 */
public class SysDeptRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long deptId;

    /** 角色Id */
    private Long roleId;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("roleId", getRoleId())
                .toString();
    }
}
