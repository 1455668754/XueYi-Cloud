package com.xueyi.tenant.mapper;

import java.util.List;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.domain.TenantSource;

/**
 * 数据源 数据层
 *
 * @author xueyi
 */
public interface TenantSourceMapper
{
    /**
     * 查询数据源列表
     * 访问控制 e 租户查询
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectTenantSourceList(TenantSource tenantSource);

    /**
     * 查询数据源
     * 访问控制 e 租户查询
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    public TenantSource selectTenantSourceById(TenantSource tenantSource);

    /**
     * 新增数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int insertTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int updateTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源排序
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int updateTenantSourceSort(TenantSource tenantSource);

    /**
     * 删除数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantSourceById(TenantSource tenantSource);

    /**
     * 批量删除数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope( ueAlias = "empty" )
    public int deleteTenantSourceByIds(TenantSource tenantSource);
}