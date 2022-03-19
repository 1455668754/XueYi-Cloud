package com.xueyi.tenant.tenant.domain.model;

import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户初始化对象
 *
 * @author xueyi
 */
public class TeTenantRegister extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 租户信息 */
    private TeTenantDto tenant;

    /** 部门信息 */
    private SysDeptDto dept;

    /** 岗位信息 */
    private SysPostDto post;

    /** 用户信息 */
    private SysUserDto user;

    /** 权限Ids */
    private Long[] authIds;

    public TeTenantDto getTenant() {
        return tenant;
    }

    public void setTenant(TeTenantDto tenant) {
        this.tenant = tenant;
    }

    public SysDeptDto getDept() {
        return dept;
    }

    public void setDept(SysDeptDto dept) {
        this.dept = dept;
    }

    public SysPostDto getPost() {
        return post;
    }

    public void setPost(SysPostDto post) {
        this.post = post;
    }

    public SysUserDto getUser() {
        return user;
    }

    public void setUser(SysUserDto user) {
        this.user = user;
    }

    public Long[] getAuthIds() {
        return authIds;
    }

    public void setAuthIds(Long[] authIds) {
        this.authIds = authIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tenant", getTenant())
                .append("dept", getDept())
                .append("post", getPost())
                .append("user", getUser())
                .append("authIds", getAuthIds())
                .toString();
    }
}
