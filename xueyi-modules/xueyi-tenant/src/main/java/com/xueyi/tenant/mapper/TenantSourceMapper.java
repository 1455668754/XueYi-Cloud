package com.xueyi.tenant.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.api.domain.source.TenantSource;

/**
 * 数据源 数据层
 *
 * @author xueyi
 */
public interface TenantSourceMapper {
    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectTenantSourceList(TenantSource tenantSource);

    /**
     * 查询数据源
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    public TenantSource selectTenantSourceById(TenantSource tenantSource);


    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectTenantSeparationList(TenantSource tenantSource);

    /**
     * 查询 只读 数据源集合
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectContainReadList(TenantSource tenantSource);

    /**
     * 查询 含写 数据源集合
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectContainWriteList(TenantSource tenantSource);

    /**
     * 查询数据源
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    public TenantSource selectTenantSeparationById(TenantSource tenantSource);

    /**
     * 新增数据源
     * 访问控制 empty 租户更新（无前缀） | 控制方法位于TenantSourceServiceImpl insertTenantSource
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int insertTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源排序
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int updateTenantSourceSort(TenantSource tenantSource);

    /**
     * 删除数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int deleteTenantSourceById(TenantSource tenantSource);

    /**
     * 批量删除数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int deleteTenantSourceByIds(TenantSource tenantSource);

    /**
     * 批量删除数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int deleteTenantSeparationByValueId(TenantSource tenantSource);

    /**
     * 批量新增写-读数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int batchTenantSeparation(TenantSource tenantSource);

    /**
     * 通过写数据源Id删除读数据源关联信息
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int deleteTenantSeparationBySourceId(TenantSource tenantSource);

    /**
     * 校验数据源是否已应用于策略
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int checkStrategySourceBySourceId(TenantSource tenantSource);

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int checkSeparationSourceByWriteId(TenantSource tenantSource);

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int checkSeparationSourceByReadId(TenantSource tenantSource);
}