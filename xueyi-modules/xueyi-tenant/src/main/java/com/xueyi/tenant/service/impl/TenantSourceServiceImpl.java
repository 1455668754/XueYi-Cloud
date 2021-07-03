package com.xueyi.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.utils.DSUtils;
import com.xueyi.tenant.api.source.Source;
import com.xueyi.tenant.api.source.TenantSourceValue;
import com.xueyi.tenant.mapper.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.tenant.mapper.TenantSourceMapper;
import com.xueyi.tenant.api.source.TenantSource;
import com.xueyi.tenant.service.ITenantSourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantSourceServiceImpl implements ITenantSourceService {

    @Autowired
    private TenantSourceMapper tenantSourceMapper;

    @Autowired
    private DataSourceMapper sourceMapper;

    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    public List<Source> selectLoadDataSources(Source source){
        return sourceMapper.selectLoadDataSources(source);
    }

    /**
     * 查询数据源列表
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public List<TenantSource> selectTenantSourceList(TenantSource tenantSource) {
        return tenantSourceMapper.selectTenantSourceList(tenantSource);
    }


    /**
     * 查询数据源
     *
     * @param tenantSource 数据源
     * @return 数据源
     */
    @Override
    public TenantSource selectTenantSourceById(TenantSource tenantSource) {
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
    public int insertTenantSource(TenantSource tenantSource) {
        if (tenantSource.getType().equals("0")) {
            TenantSourceValue value = new TenantSourceValue();
            value.setSourceId(tenantSource.getId());
            List<TenantSourceValue> values = new ArrayList<>();
            values.add(value);
            tenantSource.setValues(values);
            tenantSource.setSourceId(tenantSource.getId());
            tenantSourceMapper.batchTenantSeparation(tenantSource);
        }
        if(tenantSource.getType().equals("2")){
            tenantSource.setSlave("slave"+tenantSource.getId().toString());
        }else{
            tenantSource.setSlave("master"+tenantSource.getId().toString());
        }
        // 将数据新增的的数据源添加到数据源库
        DSUtils.addDs(tenantSource);
        return tenantSourceMapper.insertTenantSource(tenantSource);
    }

    /**
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int updateTenantSource(TenantSource tenantSource) {
        int res = tenantSourceMapper.updateTenantSource(tenantSource);
        if (res > 0)
        {
            // 根据数据源编码从数据源库中删除数据源
            DSUtils.delDs(tenantSource.getSlave());
            // 再将数据源添加到数据源库中
            DSUtils.addDs(tenantSource);
        }
        return res;
    }

    /**
     * 修改数据源排序
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int updateTenantSourceSort(TenantSource tenantSource) {
        return tenantSourceMapper.updateTenantSourceSort(tenantSource);
    }

    /**
     * 删除数据源信息
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int deleteTenantSourceById(TenantSource tenantSource) {
        int res = tenantSourceMapper.deleteTenantSourceById(tenantSource);
        if (res > 0)
        {
            // 根据数据源编码从数据源库中删除数据源
            DSUtils.delDs(tenantSource.getSlave());
        }
        return res;
    }

    /**
     * 批量删除数据源
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantSourceByIds(TenantSource tenantSource) {
        tenantSourceMapper.deleteTenantSeparationByValueId(tenantSource);
        return tenantSourceMapper.deleteTenantSourceByIds(tenantSource);
    }
}