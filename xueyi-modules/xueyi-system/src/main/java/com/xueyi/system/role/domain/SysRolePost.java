package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和岗位关联对象 sys_role_post
 *
 * @author xueyi
 */
public class SysRolePost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 岗位Id */
    private Long postId;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("postId", getPostId())
                .toString();
    }
}
