package com.xueyi.system.api.organize.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.Excel.Type;
import com.xueyi.common.core.annotation.Excels;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.web.validate.V_A_E;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.po.SysUserPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_user", excludeProperty = {"name"})
public class SysUserDto extends SysUserPo {

    private static final long serialVersionUID = 1L;

    /** 岗位对象 */
    @Excels({
            @Excel(name = "岗位名称", targetAttr = "postName", type = Type.EXPORT),
            @Excel(name = "岗位编码(*)", targetAttr = "postCode", type = Type.IMPORT)
    })
    @TableField(exist = false)
    private List<SysPostDto> posts;

    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRoleDto> roles;

    /** 岗位组 */
    @TableField(exist = false)
    private Long[] postIds;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    public SysUserDto() {
    }

    public SysUserDto(Long userId) {
        setId(userId);
    }

    public List<SysPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<SysPostDto> posts) {
        this.posts = posts;
    }

    public List<SysRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleDto> roles) {
        this.roles = roles;
    }

    @NotEmpty(message = "归属岗位不能为空", groups = {V_A_E.class})
    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public boolean isNotAdmin() {
        return !isAdmin(this.getUserType());
    }

    public boolean isAdmin() {
        return isAdmin(this.getUserType());
    }

    public static boolean isNotAdmin(String userType) {
        return !isAdmin(userType);
    }

    public static boolean isAdmin(String userType) {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), userType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userCode", getCode())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("sex", getSex())
                .append("posts", getPosts())
                .append("postIds", getPostIds())
                .append("roles", getRoles())
                .append("avatar", getAvatar())
                .append("profile", getProfile())
                .append("password", getPassword())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
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
