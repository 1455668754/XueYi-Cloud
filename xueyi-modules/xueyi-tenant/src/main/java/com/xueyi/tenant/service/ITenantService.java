package com.xueyi.tenant.service;

import java.util.List;
import com.xueyi.tenant.domain.Tenant;

/**
 * 租户信息 业务层
 *
 * @author xueyi
 */
public interface ITenantService
{
    /**
     * 查询租户信息列表
     *
     * @param tenant 租户信息
     * @return 租户信息集合
     */
    public List<Tenant> selectTenantList(Tenant tenant);

    /**
     * 查询租户信息
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    public Tenant selectTenantById(Tenant tenant);

    /**
     * 新增租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenant(Tenant tenant);

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int updateTenant(Tenant tenant);

    /**
     * 修改租户信息排序
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int updateTenantSort(Tenant tenant);

    /**
     * 删除租户信息信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int deleteTenantById(Tenant tenant);

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int deleteTenantByIds(Tenant tenant);
}