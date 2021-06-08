package com.xueyi.tenant.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.domain.TenantStrategy;
import com.xueyi.tenant.domain.TenantSource;

/**
 * 数据源策略 数据层
 *
 * @author xueyi
 */
public interface TenantStrategyMapper {
    /**
     * 查询数据源策略列表
     * 访问控制 e 租户查询
     *
     * @param tenantStrategy 数据源策略
     * @return 数据源策略集合
     */
    @DataScope(eAlias = "e")
    public List<TenantStrategy> selectTenantStrategyList(TenantStrategy tenantStrategy);

    /**
     * 查询数据源策略
     * 访问控制 e 租户查询
     *
     * @param tenantStrategy 数据源策略
     * @return 数据源策略
     */
    @DataScope(eAlias = "e")
    public TenantStrategy selectTenantStrategyById(TenantStrategy tenantStrategy);

    /**
     * 新增数据源策略
     * 访问控制 empty 租户更新（无前缀）()控制方法在impl层 | TenantStrategyServiceImpl)
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    public int insertTenantStrategy(TenantStrategy tenantStrategy);

    /**
     * 修改数据源策略
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateTenantStrategy(TenantStrategy tenantStrategy);

    /**
     * 修改数据源策略排序
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateTenantStrategySort(TenantStrategy tenantStrategy);

    /**
     * 删除数据源策略
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantStrategyById(TenantStrategy tenantStrategy);

    /**
     * 批量删除数据源策略
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantStrategyByIds(TenantStrategy tenantStrategy);

    /**
     * 批量新增数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int batchTenantSource(TenantStrategy tenantStrategy);

    /**
     * 通过数据源策略Id删除数据源信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantSourceByStrategyId(TenantStrategy tenantStrategy);

    /**
     * 批量删除数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantSourceByStrategyIds(TenantStrategy tenantStrategy);
}