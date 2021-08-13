package com.xueyi.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.utils.DSUtils;
import com.xueyi.tenant.api.domain.source.TenantSourceValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.tenant.mapper.TenantSourceMapper;
import com.xueyi.tenant.api.domain.source.TenantSource;
import com.xueyi.tenant.service.ITenantSourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TenantSourceServiceImpl implements ITenantSourceService {

    @Autowired
    private TenantSourceMapper tenantSourceMapper;

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
        if (tenantSource.getType().equals("2")) {
            tenantSource.setSlave("slave" + tenantSource.getId().toString());
        } else {
            tenantSource.setSlave("master" + tenantSource.getId().toString());
        }
        // 将数据新增的的数据源添加到数据源库
        tenantSource.setSyncType(TenantConstants.SYNC_TYPE_ADD);
        DSUtils.addDs(tenantSource);
        DSUtils.syncDS(tenantSource);
        return tenantSourceMapper.insertTenantSource(tenantSource);
    }

    /**
     * 修改数据源
     *
     * @param tenantSource 数据源
     * @param ds           数据源新增|更新|删除判断
     * @return 结果
     */
    @Override
    public int updateTenantSource(TenantSource tenantSource, int ds) {
        int res = tenantSourceMapper.updateTenantSource(tenantSource);
        if (res > 0 && ds != TenantConstants.SYNC_TYPE_UNCHANGED) {
            //ds 0不变 1刷新 2启动 3删除
            if (ds == TenantConstants.SYNC_TYPE_REFRESH) {
                DSUtils.delDs(tenantSource.getSlave());
                DSUtils.addDs(tenantSource);
            } else if (ds == TenantConstants.SYNC_TYPE_ADD) {
                DSUtils.addDs(tenantSource);
            } else if (ds == TenantConstants.SYNC_TYPE_DELETE) {
                DSUtils.delDs(tenantSource.getSlave());
            }
            DSUtils.syncDS(tenantSource);
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
    @Transactional
    public int deleteTenantSourceById(TenantSource tenantSource) {
        tenantSourceMapper.deleteTenantSeparationByValueId(tenantSource);
        int res = tenantSourceMapper.deleteTenantSourceById(tenantSource);
        if (res > 0) {
            tenantSource.setSyncType(TenantConstants.SYNC_TYPE_DELETE);
            DSUtils.delDs(tenantSource.getSlave());
            DSUtils.syncDS(tenantSource);
        }
        return res;
    }

    /**
     * 批量删除数据源
     *
     * @param tenantSource 数据源
     * @param DsIds        需停用的数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantSourceByIds(TenantSource tenantSource, List<TenantSource> DsIds) {
        tenantSourceMapper.deleteTenantSeparationByValueId(tenantSource);
        int res = tenantSourceMapper.deleteTenantSourceByIds(tenantSource);
        if (res > 0 && DsIds.size() > 0) {
            for (TenantSource Ds : DsIds) {
                Ds.setSyncType(TenantConstants.SYNC_TYPE_DELETE);
                DSUtils.delDs(Ds.getSlave());
                DSUtils.syncDS(Ds);
            }
        }
        return res;
    }

    /**
     * 校验数据源是否已应用于策略
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int checkStrategySourceBySourceId(TenantSource tenantSource) {
        return tenantSourceMapper.checkStrategySourceBySourceId(tenantSource);
    }

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int checkSeparationSourceByWriteId(TenantSource tenantSource) {
        return tenantSourceMapper.checkSeparationSourceByWriteId(tenantSource);
    }

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param tenantSource 数据源
     * @return 结果
     */
    @Override
    public int checkSeparationSourceByReadId(TenantSource tenantSource) {
        return tenantSourceMapper.checkSeparationSourceByReadId(tenantSource);
    }
}