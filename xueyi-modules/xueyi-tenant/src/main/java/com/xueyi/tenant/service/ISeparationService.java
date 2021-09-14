package com.xueyi.tenant.service;

import java.util.List;

import com.xueyi.tenant.api.domain.source.Source;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ISeparationService {

    /**
     * 查询 只读 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> mainSelectContainReadList(Source source);

    /**
     * 查询 含写 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> mainSelectContainWriteList(Source source);

    /**
     * 根据Id查询数据源及其分离策略
     *
     * @param source 数据源
     * @return 数据源
     */
    public Source mainSelectSeparationById(Source source);

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainUpdateSeparation(Source source);
}