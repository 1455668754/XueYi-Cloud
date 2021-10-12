package com.xueyi.tenant.mapper;

import com.xueyi.tenant.domain.Tenant;


/**
 * 租户新增同步创建信息 数据层
 *
 * @author xueyi
 */
public interface CreationMapper {

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

    /**
     * 创建新企业衍生角色
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createRoleByTenantId(Tenant tenant);

    /**
     * 创建新租户组织-衍生角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createOrganizeRoleByTenantId(Tenant tenant);
}