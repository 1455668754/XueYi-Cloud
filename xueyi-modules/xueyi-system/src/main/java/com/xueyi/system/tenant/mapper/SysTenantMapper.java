package com.xueyi.system.tenant.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.tenant.domain.SysTenant;

import java.util.List;

/**
 * 租户管理 数据层
 *
 * @author xueyi
 */
public interface SysTenantMapper {
    /**
     * 查询租户管理
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    public SysTenant selectSysTenantById(SysTenant sysTenant);

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int insertSysTenant(SysTenant sysTenant);

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int insertSysTenantDatabase(SysTenant sysTenant);

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int updateSysTenant(SysTenant sysTenant);

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int updateSysTenantDatabase(SysTenant sysTenant);

    /**
     * 删除租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int deleteSysTenantById(SysTenant sysTenant);

    /**
     * 删除租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int deleteSysTenantDatabaseById(SysTenant sysTenant);

    /**
     * 创建新租户部门
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int createDeptByTenantId(SysTenant sysTenant);

    /**
     * 创建新租户岗位
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int createPostByTenantId(SysTenant sysTenant);

    /**
     * 创建新租户员工
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int createUserByTenantId(SysTenant sysTenant);
}