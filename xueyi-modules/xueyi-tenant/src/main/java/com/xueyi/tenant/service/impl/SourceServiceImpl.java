package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.utils.DSUtils;
import com.xueyi.tenant.api.domain.source.Source;
import com.xueyi.tenant.mapper.SourceMapper;
import com.xueyi.tenant.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        source.setSlave((source.getType().equals(TenantConstants.SOURCE_READ) ? "slave" : "master") + source.getSnowflakeId().toString());
        // 将数据新增的的数据源添加到数据源库
        source.setSyncType(TenantConstants.SYNC_TYPE_ADD);
        DSUtils.syncDs(source);
        if (source.getType().equals(TenantConstants.SOURCE_READ_WRITE)) {
            Source value = new Source(source.getSnowflakeId());
            value.setSlave(source.getSlave());
            List<Source> values = new ArrayList<>();
            values.add(value);
            source.setValues(values);
            source.setSourceId(source.getSnowflakeId());
            sourceMapper.mainBatchSeparation(source);
        }
        return sourceMapper.mainInsertSource(source);
    }

    /**
     * 修改数据源
     *
     * @param source 数据源 | sourceId 数据源Id | name 数据源名称
     * @return 结果
     */
    @Override
    public int mainUpdateSource(Source source) {
        return sourceMapper.mainUpdateSource(source);
    }

    /**
     * 启用/禁用数据源
     *
     * @param source 数据源 | sourceId 数据源Id | status 状态 | isChange 系统默认
     * @return 结果
     */
    @Override
    public int mainUpdateSourceStatus(Source source) {
        int rows = sourceMapper.mainUpdateSourceStatus(source);
        if (rows > 0) {
            DSUtils.syncDs(source);
        }
        return rows;
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
        DSUtils.syncDs(source);
        sourceMapper.mainDeleteSeparationByValueId(source);
        return sourceMapper.mainDeleteSourceById(source);
    }

    /**
     * 校验数据源是否已应用于策略
     *
     * @param source 数据源
     * @return 结果 (true 已应用 false 未应用)
     */
    @Override
    public boolean mainCheckStrategySourceBySourceId(Source source) {
        return sourceMapper.mainCheckStrategySourceBySourceId(source) > 0;
    }

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param source 数据源
     * @return 结果 (true 已设置 false 未设置)
     */
    @Override
    public boolean mainCheckSeparationSourceByWriteId(Source source) {
        return sourceMapper.mainCheckSeparationSourceByWriteId(source) > 0;
    }

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param source 数据源
     * @return 结果 (true 已应用 false 未应用)
     */
    @Override
    public boolean mainCheckSeparationSourceByReadId(Source source) {
        return sourceMapper.mainCheckSeparationSourceByReadId(source) > 0;
    }
}