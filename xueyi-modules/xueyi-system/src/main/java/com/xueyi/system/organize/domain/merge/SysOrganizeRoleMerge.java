package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import com.xueyi.common.security.utils.SecurityUtils;

/**
 * 组织-角色关联（角色绑定） 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_organize_role_merge")
public class SysOrganizeRoleMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 部门Id */
    @TableField("dept_id")
    private Long deptId;

    /** 岗位Id */
    @TableField("post_id")
    private Long postId;

    /** 用户Id */
    @TableField("user_id")
    private Long userId;

    /** 角色Id */
    @TableField("role_id")
    private Long roleId;

    public SysOrganizeRoleMerge() {
    }

    public SysOrganizeRoleMerge(Long organizeId, Long roleId, OrganizeConstants.OrganizeType organizeType) {
        switch (organizeType) {
            case DEPT:
                setDeptId(organizeId);
                break;
            case POST:
                setPostId(organizeId);
                break;
            case USER:
                setUserId(organizeId);
                break;
        }
        setRoleId(roleId);
        setEnterpriseId(SecurityUtils.getEnterpriseId());
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
