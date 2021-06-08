package com.xueyi.tenant.mapper;

import com.xueyi.tenant.domain.Tenant;

public interface NewTenantMapper {

    /**
     * 创建新租户部门
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createDeptByTenantId(Tenant tenant);

    /**
     * 创建新租户岗位
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createPostByTenantId(Tenant tenant);

    /**
     * 创建新租户员工
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createUserByTenantId(Tenant tenant);
}
