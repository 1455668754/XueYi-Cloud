package com.xueyi.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.utils.DSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.tenant.mapper.SourceMapper;
import com.xueyi.tenant.api.domain.source.Source;
import com.xueyi.tenant.service.ISourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SourceServiceImpl implements ISourceService {

    @Autowired
    private SourceMapper sourceMapper;

    /**
     * 查询数据源列表
     *
     * @param source 数据源
     * @return 数据源
     */
    @Override
    public List<Source> mainSelectSourceList(Source source) {
        return sourceMapper.mainSelectSourceList(source);
    }

    /**
     * 根据源Id查询数据源信息
     *
     * @param source 数据源
     * @return 数据源
     */
    @Override
    public Source mainSelectSourceBySourceId(Source source) {
        return sourceMapper.mainSelectSourceBySourceId(source);
    }

    /**
     * 新增数据源
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int mainInsertSource(Source source) {
        source.setSlave((source.getType().equals(TenantConstants.SOURCE_WRITE)? "slave": "master") + source.getSnowflakeId().toString());
        // 将数据新增的的数据源添加到数据源库
        source.setSyncType(TenantConstants.SYNC_TYPE_ADD);
        DSUtils.addDs(source);
        DSUtils.syncDS(source);
        if (source.getType().equals(TenantConstants.SOURCE_READ_WRITE)) {
            Source value = new Source();
            value.setSourceId(source.getSnowflakeId());
            List<Source> values = new ArrayList<>();
            values.add(value);
            source.setValues(values);
            source.setSourceId(source.getSnowflakeId());
            sourceMapper.mainBatchTenantSeparation(source);
        }
        return sourceMapper.mainInsertSource(source);
    }

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @param ds           数据源新增|更新|删除判断
     * @return 结果
     */
    @Override
    public int mainUpdateSource(Source source, int ds) {

        if (ds != TenantConstants.SYNC_TYPE_UNCHANGED) {
            if (ds == TenantConstants.SYNC_TYPE_REFRESH) {
                DSUtils.delDs(source.getSlave());
                DSUtils.addDs(source);
            } else if (ds == TenantConstants.SYNC_TYPE_ADD) {
                DSUtils.addDs(source);
            } else if (ds == TenantConstants.SYNC_TYPE_DELETE) {
                DSUtils.delDs(source.getSlave());
            }
            DSUtils.syncDS(source);
        }
        return sourceMapper.mainUpdateSource(source);
    }

    /**
     * 修改数据源排序
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    public int mainUpdateSourceSort(Source source) {
        return sourceMapper.mainUpdateSourceSort(source);
    }

    /**
     * 删除数据源信息
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int mainDeleteSourceById(Source source) {
        source.setSyncType(TenantConstants.SYNC_TYPE_DELETE);
        DSUtils.delDs(source.getSlave());
        DSUtils.syncDS(source);
        sourceMapper.mainDeleteTenantSeparationByValueId(source);
        return sourceMapper.mainDeleteSourceById(source);
    }

    /**
     * 批量删除数据源
     *
     * @param DsIds 需停用的数据源集合
     * @return 结果
     */
    @Override
    @Transactional
    public int mainDeleteSourceByIds(List<Source> DsIds) {
        for (Source Ds : DsIds) {
            Ds.setSyncType(TenantConstants.SYNC_TYPE_DELETE);
            DSUtils.delDs(Ds.getSlave());
            DSUtils.syncDS(Ds);
            sourceMapper.mainDeleteSourceById(Ds);
            sourceMapper.mainDeleteTenantSeparationByValueId(Ds);
        }
        return DsIds.size();
    }

    /**
     * 校验数据源是否已应用于策略
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    public int mainCheckStrategySourceBySourceId(Source source) {
        return sourceMapper.mainCheckStrategySourceBySourceId(source);
    }

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    public int mainCheckSeparationSourceByWriteId(Source source) {
        return sourceMapper.mainCheckSeparationSourceByWriteId(source);
    }

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    public int mainCheckSeparationSourceByReadId(Source source) {
        return sourceMapper.mainCheckSeparationSourceByReadId(source);
    }
}