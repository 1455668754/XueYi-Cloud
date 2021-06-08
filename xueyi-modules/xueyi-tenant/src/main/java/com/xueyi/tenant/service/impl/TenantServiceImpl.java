package com.xueyi.tenant.service.impl;

import java.util.List;

import cn.hutool.core.util.IdUtil;
import com.xueyi.tenant.mapper.NewTenantMapper;
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
public class TenantServiceImpl implements ITenantService {
    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private NewTenantMapper newTenantMapper;

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
    @DataScope(ueAlias = "empty")
    public int insertTenant(Tenant tenant) {
        int rows = tenantMapper.insertTenant(tenant);
        /**获取生成雪花Id，并赋值给主键，加入至子表对应外键中*/
        tenant.setTenantId(tenant.getId());
        if (tenant.getValues().size() > 0) {
            tenantMapper.batchTenantStrategy(tenant);
        }

        //新建租户时同步新建信息
        //1.新建租户的部门|岗位|超管用户信息
        tenant.getParams().put("deptId", IdUtil.getSnowflake(0, 0).nextId());
        tenant.getParams().put("postId", IdUtil.getSnowflake(0, 0).nextId());
        tenant.getParams().put("userId", IdUtil.getSnowflake(0, 0).nextId());
        newTenantMapper.createDeptByTenantId(tenant);
        newTenantMapper.createPostByTenantId(tenant);
        newTenantMapper.createUserByTenantId(tenant);

        return rows;
    }

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTenant(Tenant tenant) {
        tenantMapper.deleteTenantStrategyByTenantId(tenant);
        if (tenant.getValues().size() > 0) {
            tenantMapper.batchTenantStrategy(tenant);
        }
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
    @Transactional
    public int deleteTenantById(Tenant tenant) {
        tenantMapper.deleteTenantStrategyByTenantId(tenant);
        return tenantMapper.deleteTenantById(tenant);
    }

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantByIds(Tenant tenant) {
        tenantMapper.deleteTenantStrategyByTenantIds(tenant);
        return tenantMapper.deleteTenantByIds(tenant);
    }
}