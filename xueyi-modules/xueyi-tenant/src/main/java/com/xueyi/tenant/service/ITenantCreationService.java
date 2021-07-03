package com.xueyi.tenant.service;

import com.xueyi.tenant.domain.Tenant;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ITenantCreationService {

    /**
     * 租户新增-创建组织信息
     *
     * @param sourceName 数据源名称
     * @param tenant     租户信息
     * @return 结果
     */
    public int organizeCreation(String sourceName, Tenant tenant);
}