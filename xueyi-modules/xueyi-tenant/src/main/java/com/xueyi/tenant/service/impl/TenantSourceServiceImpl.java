package com.xueyi.tenant.service.impl;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.domain.TenantSourceValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.tenant.mapper.TenantSourceMapper;
import com.xueyi.tenant.domain.TenantSource;
import com.xueyi.tenant.service.ITenantSourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantSourceServiceImpl implements ITenantSourceService
{
    @Autowired
    private TenantSourceMapper tenantSourceMapper;

    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public List<TenantSource> selectTenantSourceList(TenantSource tenantSource)
    {
        return tenantSourceMapper.selectTenantSourceList(tenantSource);
    }


    /**
     * 查询数据源
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public TenantSource selectTenantSourceById(TenantSource tenantSource)
    {
        return tenantSourceMapper.selectTenantSourceById(tenantSource);
    }

    /**
     * 新增数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertTenantSource(TenantSource tenantSource)
    {
        TenantSourceValue value = new TenantSourceValue();
        value.setSourceId(tenantSource.getId());
        tenantSource.getValues().add(value);
        tenantSourceMapper.batchTenantSeparation(tenantSource);
        return tenantSourceMapper.insertTenantSource(tenantSource);
    }

    /**
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int updateTenantSource(TenantSource tenantSource)
    {
        return tenantSourceMapper.updateTenantSource(tenantSource);
    }

    /**
     * 修改数据源排序
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int updateTenantSourceSort(TenantSource tenantSource){
        return tenantSourceMapper.updateTenantSourceSort(tenantSource);
    }

    /**
     * 删除数据源信息
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int deleteTenantSourceById(TenantSource tenantSource)
    {
        return tenantSourceMapper.deleteTenantSourceById(tenantSource);
    }

    /**
     * 批量删除数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int deleteTenantSourceByIds(TenantSource tenantSource)
    {
        return tenantSourceMapper.deleteTenantSourceByIds(tenantSource);
    }
}