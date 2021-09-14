package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.tenant.api.domain.source.Source;
import com.xueyi.tenant.mapper.SourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.service.ISeparationService;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SeparationServiceImpl implements ISeparationService {

    @Autowired
    private SourceMapper sourceMapper;

    /**
     * 查询 只读 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    @Override
    public List<Source> mainSelectContainReadList(Source source) {
        return sourceMapper.mainSelectContainReadList(source);
    }

    /**
     * 查询 含写 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    @Override
    public List<Source> mainSelectContainWriteList(Source source) {
        return sourceMapper.mainSelectContainWriteList(source);
    }

    /**
     * 根据Id查询数据源及其分离策略
     *
     * @param source 数据源
     * @return 数据源
     */
    @Override
    public Source mainSelectSeparationById(Source source) {
        return sourceMapper.mainSelectSeparationById(source);
    }

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @return 结果
     */
    @Override
    @Transactional
    public int mainUpdateSeparation(Source source) {
        int k= sourceMapper.mainDeleteSeparationBySourceId(source);
        if (source.getValues() != null && source.getValues().size() > 0) {
            sourceMapper.mainBatchSeparation(source);
        }
        return k;
    }
}