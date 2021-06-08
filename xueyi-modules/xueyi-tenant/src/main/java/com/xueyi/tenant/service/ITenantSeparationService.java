package com.xueyi.tenant.service;

import java.util.List;
import com.xueyi.tenant.domain.TenantSource;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ITenantSeparationService {
    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    public List<TenantSource> selectTenantSeparationList(TenantSource tenantSource);

    /**
     * 查询 含读 数据源集合
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
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    public int updateTenantSeparation(TenantSource tenantSource);
}