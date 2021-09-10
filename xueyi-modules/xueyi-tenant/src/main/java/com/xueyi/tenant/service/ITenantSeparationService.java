package com.xueyi.tenant.service;

import java.util.List;

import com.xueyi.tenant.api.domain.source.Source;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ITenantSeparationService {

    /**
     * 查询数据源列表
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> mainSelectSeparationList(Source source);

    /**
     * 查询 只读 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> selectContainReadList(Source source);

    /**
     * 查询 含写 数据源集合
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> selectContainWriteList(Source source);

    /**
     * 查询数据源
     *
     * @param source 数据源
     * @return 数据源
     */
    public Source selectTenantSeparationById(Source source);

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int updateTenantSeparation(Source source);
}