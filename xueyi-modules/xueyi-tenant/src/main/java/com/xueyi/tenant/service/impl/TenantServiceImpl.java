package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.tenant.api.source.TenantSource;
import com.xueyi.tenant.domain.TenantStrategy;
import com.xueyi.tenant.mapper.TenantStrategyMapper;
import com.xueyi.tenant.service.ITenantCreationService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.datascope.annotation.DataScope;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.mapper.TenantMapper;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.service.ITenantService;

/**
 * 租户信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TenantServiceImpl implements ITenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private TenantStrategyMapper tenantStrategyMapper;

    @Autowired
    private ITenantCreationService tenantCreationService;

    /**
     * 查询租户信息列表
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<Tenant> selectTenantList(Tenant tenant) {
        return tenantMapper.selectTenantList(tenant);
    }

    /**
     * 查询租户信息
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public Tenant selectTenantById(Tenant tenant) {
        return tenantMapper.selectTenantById(tenant);
    }

    /**
     * 新增租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    @GlobalTransactional        // 第一个开启事务的需要添加seata全局事务注解
    @DataScope(ueAlias = "empty")
    public int insertTenant(Tenant tenant) {
        /* 获取生成雪花Id，并赋值给主键，加入至子表对应外键中 */
        tenant.setTenantId(tenant.getId());
        TenantStrategy search = new TenantStrategy();
        search.setStrategyId(tenant.getStrategyId());
        TenantStrategy strategy = tenantStrategyMapper.selectTenantStrategyById(search);
        for (TenantSource source : strategy.getValues()) {
            if (source.getIsMain().equals("Y")) {
                String sourceName = source.getSlave();
                //新建租户时同步新建信息
                //1.新建租户的部门|岗位|超管用户信息
                tenantCreationService.organizeCreation(sourceName, tenant);
                return tenantMapper.insertTenant(tenant);
            }
        }
        try {
            throw new CustomException(String.format("创建失败，%1$s未设置主数据源,无法应用该策略", strategy.getName()));
        } catch (Exception ignored) {
        }
        return 0;
    }

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int updateTenant(Tenant tenant) {
        return tenantMapper.updateTenant(tenant);
    }

    /**
     * 修改租户信息排序
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int updateTenantSort(Tenant tenant) {
        return tenantMapper.updateTenantSort(tenant);
    }

    /**
     * 删除租户信息信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int deleteTenantById(Tenant tenant) {
        return tenantMapper.deleteTenantById(tenant);
    }

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int deleteTenantByIds(Tenant tenant) {
        return tenantMapper.deleteTenantByIds(tenant);
    }
}