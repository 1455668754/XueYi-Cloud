package com.xueyi.tenant.service.impl;

import java.util.List;

import com.xueyi.tenant.api.source.TenantSource;
import com.xueyi.tenant.mapper.TenantSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.service.ITenantSeparationService;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantSeparationServiceImpl implements ITenantSeparationService {
    @Autowired
    private TenantSourceMapper tenantSourceMapper;

    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public List<TenantSource> selectTenantSeparationList(TenantSource tenantSource) {
        return tenantSourceMapper.selectTenantSeparationList(tenantSource);
    }

    /**
     * 查询 含读 数据源集合
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    @Override
    public List<TenantSource> selectContainReadList(TenantSource tenantSource) {
        return tenantSourceMapper.selectContainReadList(tenantSource);
    }

    /**
     * 查询 含写 数据源集合
     *
     * @param tenantSource 数据源
     * @return 数据源集合
     */
    @Override
    public List<TenantSource> selectContainWriteList(TenantSource tenantSource) {
        return tenantSourceMapper.selectContainWriteList(tenantSource);
    }

    /**
     * 查询数据源
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public TenantSource selectTenantSeparationById(TenantSource tenantSource) {
        return tenantSourceMapper.selectTenantSeparationById(tenantSource);
    }

    /**
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTenantSeparation(TenantSource tenantSource) {
        int k=tenantSourceMapper.deleteTenantSeparationBySourceId(tenantSource);
        if (tenantSource.getValues().size() > 0) {
            tenantSourceMapper.batchTenantSeparation(tenantSource);
        }
        return k;
    }
}