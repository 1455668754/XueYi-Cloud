package com.xueyi.system.role.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 岗位和角色关联对象 sys_post_role
 *
 * @author xueyi
 */
public class SysPostRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 岗位Id */
    private Long postId;

    /** 角色Id */
    private Long roleId;

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
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
                .append("postId", getPostId())
                .append("roleId", getRoleId())
                .toString();
    }
}
