package com.xueyi.tenant.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.mapper.TenantCreationMapper;
import com.xueyi.tenant.service.ITenantCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantCreationServiceImpl implements ITenantCreationService {

    @Autowired
    private TenantCreationMapper tenantCreationMapper;

    /**
     * 租户新增-创建组织信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @DS("#tenant.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)      // 事务传播特性设置为 REQUIRES_NEW 开启新的事务
    public int organizeCreation(Tenant tenant) {
        int rows = 0;
        tenant.getParams().put("deptId", IdUtil.getSnowflake(0, 0).nextId());
        tenant.getParams().put("postId", IdUtil.getSnowflake(0, 0).nextId());
        tenant.getParams().put("userId", IdUtil.getSnowflake(0, 0).nextId());
        rows = rows + tenantCreationMapper.createDeptByTenantId(tenant);
        rows = rows + tenantCreationMapper.createPostByTenantId(tenant);
        rows = rows + tenantCreationMapper.createUserByTenantId(tenant);
        return rows;
    }
}