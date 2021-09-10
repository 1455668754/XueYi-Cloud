package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.api.domain.source.Source;
import com.xueyi.tenant.mapper.SourceMapper;
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
@DS("#main")
public class TenantSeparationServiceImpl implements ITenantSeparationService {

    @Autowired
    private SourceMapper tenantSourceMapper;

    /**
     * 查询数据源列表
     *
     * @param source 数据源
     * @return 数据源
     */
    @Override
    public List<Source> mainSelectSeparationList(Source source) {
        return tenantSourceMapper.mainSelectSeparationList(source);
    }

    /**
     * 查询 只读 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    @Override
    public List<Source> selectContainReadList(Source source) {
        return tenantSourceMapper.mainSelectContainReadList(source);
    }

    /**
     * 查询 含写 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    @Override
    public List<Source> selectContainWriteList(Source source) {
        return tenantSourceMapper.mainSelectContainWriteList(source);
    }

    /**
     * 查询数据源
     *
     * @param source 数据源
     * @return 数据源
     */
    @Override
    public Source selectTenantSeparationById(Source source) {
        return tenantSourceMapper.mainSelectTenantSeparationById(source);
    }

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTenantSeparation(Source source) {
        int k=tenantSourceMapper.mainDeleteTenantSeparationBySourceId(source);
        if (source.getValues() != null && source.getValues().size() > 0) {
            tenantSourceMapper.mainBatchTenantSeparation(source);
        }
        return k;
    }
}