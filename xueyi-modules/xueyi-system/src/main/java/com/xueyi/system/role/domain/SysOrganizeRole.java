package com.xueyi.system.role.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.web.domain.BaseEntity;

/**
 * 组织和角色关联对象 sys_organize_role
 *
 * @author xueyi
 */
public class SysOrganizeRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long deptId;

    /** 岗位id */
    private Long postId;

    /** 用户id */
    private Long userId;

    /** 部门衍生id */
    private Long deriveDeptId;

    /** 岗位衍生id */
    private Long derivePostId;

    /** 用户衍生id */
    private Long deriveUserId;

    /** 企业衍生id */
    private Long deriveEnterpriseId;

    /** 租户衍生id */
    private Long deriveTenantId;

    /** 角色Id */
    private Long roleId;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setDeriveDeptId(Long deriveDeptId) {
        this.deriveDeptId = deriveDeptId;
    }

    public Long getDeriveDeptId() {
        return deriveDeptId;
    }

    public void setDerivePostId(Long derivePostId) {
        this.derivePostId = derivePostId;
    }

    public Long getDerivePostId() {
        return derivePostId;
    }

    public void setDeriveUserId(Long deriveUserId) {
        this.deriveUserId = deriveUserId;
    }

    public Long getDeriveUserId() {
        return deriveUserId;
    }

    public Long getDeriveEnterpriseId() {
        return deriveEnterpriseId;
    }

    public void setDeriveEnterpriseId(Long deriveEnterpriseId) {
        this.deriveEnterpriseId = deriveEnterpriseId;
    }

    public void setDeriveTenantId(Long deriveTenantId) {
        this.deriveTenantId = deriveTenantId;
    }

    public Long getDeriveTenantId() {
        return deriveTenantId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("postId", getPostId())
                .append("userId", getUserId())
                .append("deriveDeptId", getDeriveDeptId())
                .append("derivePostId", getDerivePostId())
                .append("deriveUserId", getDeriveUserId())
                .append("deriveEnterpriseId", getDeriveEnterpriseId())
                .append("deriveAdministratorId", getDeriveTenantId())
                .append("roleId", getRoleId())
                .toString();
    }
}