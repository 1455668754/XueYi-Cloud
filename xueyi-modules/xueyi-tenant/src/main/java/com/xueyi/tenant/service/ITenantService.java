package com.xueyi.tenant.service;

import java.util.List;

import com.xueyi.tenant.domain.Tenant;

/**
 * 租户信息 业务层
 *
 * @author xueyi
 */
public interface ITenantService {

    /**
     * 查询租户信息列表
     *
     * @param tenant 租户信息
     * @return 租户信息集合
     */
    public List<Tenant> mainSelectTenantList(Tenant tenant);

    /**
     * 查询租户信息
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    public Tenant mainSelectTenantById(Tenant tenant);

    /**
     * 新增租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainInsertTenant(Tenant tenant);

    /**
     * 注册租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public Boolean mainRegisterTenant(Tenant tenant);

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainUpdateTenant(Tenant tenant);

    /**
     * 修改租户信息排序
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainUpdateTenantSort(Tenant tenant);

    /**
     * 新增指定租户Id cache
     *
     * @param tenantId 租户Id
     */
    public void refreshTenantCache(Long tenantId);
    /**
     * 删除租户信息信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainDeleteTenantById(Tenant tenant);

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainDeleteTenantByIds(Tenant tenant);

    /**
     * 校验租户账号是否唯一
     *
     * @param tenant 租户信息 | tenantName 租户Id
     * @return 结果
     */
    public String mainCheckTenantNameUnique(Tenant tenant);
}