package com.xueyi.tenant.mapper;

import java.util.List;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.domain.TenantStrategy;

/**
 * 租户信息 数据层
 *
 * @author xueyi
 */
public interface TenantMapper
{
    /**
     * 查询租户信息列表
     * 访问控制 e 租户查询
     *
     * @param tenant 租户信息
     * @return 租户信息集合
     */
    @DataScope( eAlias = "e" )
    public List<Tenant> selectTenantList(Tenant tenant);

    /**
     * 查询租户信息
     * 访问控制 e 租户查询
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @DataScope( eAlias = "e" )
    public Tenant selectTenantById(Tenant tenant);

    /**
     * 新增租户信息
     * 访问控制 empty 租户更新（无前缀）()控制方法在impl层 | TenantServiceImpl)
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenant(Tenant tenant);

    /**
     * 修改租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int updateTenant(Tenant tenant);

    /**
     * 修改租户信息排序
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int updateTenantSort(Tenant tenant);

    /**
     * 删除租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantById(Tenant tenant);

    /**
     * 批量删除租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantByIds(Tenant tenant);

    /**
     * 批量新增数据源策略
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int batchTenantStrategy(Tenant tenant);

    /**
     * 通过租户信息Id删除数据源策略信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantStrategyByTenantId(Tenant tenant);

    /**
     * 批量删除数据源策略
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantStrategyByTenantIds(Tenant tenant);
}