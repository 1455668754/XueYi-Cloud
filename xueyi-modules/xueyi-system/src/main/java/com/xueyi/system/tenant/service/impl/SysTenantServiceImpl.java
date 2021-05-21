package com.xueyi.system.tenant.service.impl;

import com.xueyi.system.tenant.domain.SysTenant;
import com.xueyi.system.tenant.mapper.SysTenantMapper;
import com.xueyi.system.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService
{
    @Autowired
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询租户管理
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public SysTenant selectSysTenantById(SysTenant sysTenant)
    {
        return sysTenantMapper.selectSysTenantById(sysTenant);
    }

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant)
    {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    public int insertSysTenant(SysTenant sysTenant)
    {
        return sysTenantMapper.insertSysTenant(sysTenant);
    }

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    public int updateSysTenant(SysTenant sysTenant)
    {
        return sysTenantMapper.updateSysTenant(sysTenant);
    }

    /**
     * 删除租户管理信息
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    public int deleteSysTenantById(SysTenant sysTenant)
    {
        return sysTenantMapper.deleteSysTenantById(sysTenant);
    }
}