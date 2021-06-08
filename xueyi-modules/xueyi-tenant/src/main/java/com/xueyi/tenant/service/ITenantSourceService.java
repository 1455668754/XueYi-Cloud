package com.xueyi.tenant.service;

import java.util.List;
import com.xueyi.tenant.domain.TenantSource;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ITenantSourceService {
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
     * 新增数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int insertTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int updateTenantSource(TenantSource tenantSource);

    /**
     * 修改数据源排序
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int updateTenantSourceSort(TenantSource tenantSource);

    /**
     * 删除数据源信息
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
}