package com.xueyi.system.tenant.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.tenant.domain.SysTenant;

import java.util.List;

/**
 * 租户管理 数据层
 *
 * @author xueyi
 */
public interface SysTenantMapper
{
    /**
     * 查询租户管理
     * 访问控制 e 租户查询
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @DataScope(eAlias = "e")
    public SysTenant selectSysTenantById(SysTenant sysTenant);

    /**
     * 查询租户管理列表
     * 访问控制 e 租户查询
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    @DataScope(eAlias = "e")
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertSysTenant(SysTenant sysTenant);

    /**
     * 新增租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertSysTenantDatabase(SysTenant sysTenant);

    /**
     * 修改租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSysTenant(SysTenant sysTenant);

    /**
     * 修改租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateSysTenantDatabase(SysTenant sysTenant);

    /**
     * 删除租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSysTenantById(SysTenant sysTenant);

    /**
     * 删除租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteSysTenantDatabaseById(SysTenant sysTenant);
}
