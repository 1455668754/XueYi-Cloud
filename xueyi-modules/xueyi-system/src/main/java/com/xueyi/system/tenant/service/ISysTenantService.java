package com.xueyi.system.tenant.service;

import com.xueyi.system.tenant.domain.SysTenant;

import java.util.List;

/**
 * 租户管理 业务层
 *
 * @author xueyi
 */
public interface ISysTenantService
{
    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理集合
     */
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 查询租户管理
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    public SysTenant selectSysTenantById(SysTenant sysTenant);

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int insertSysTenant(SysTenant sysTenant);

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int updateSysTenant(SysTenant sysTenant);

    /**
     * 删除租户管理信息
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    public int deleteSysTenantById(SysTenant sysTenant);
}