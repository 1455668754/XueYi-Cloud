package com.xueyi.system.api.organize.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 部门 数据传输对象
 *
 * @author xueyi
 */
@TableName("sys_dept")
public class SysDeptDto extends SysDeptPo<SysDeptDto, SysPostDto> {

    private static final long serialVersionUID = 1L;

    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRoleDto> roles;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    public List<SysRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleDto> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("code", getCode())
                .append("name", getName())
                .append("ancestors", getAncestors())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
