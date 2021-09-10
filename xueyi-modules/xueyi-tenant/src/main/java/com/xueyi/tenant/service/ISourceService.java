package com.xueyi.tenant.service;

import java.util.List;

import com.xueyi.tenant.api.domain.source.Source;

/**
 * 数据源 业务层
 *
 * @author xueyi
 */
public interface ISourceService {

    /**
     * 查询数据源列表
     *
     * @param source 数据源
     * @return 数据源集合
     */
    public List<Source> mainSelectSourceList(Source source);

    /**
     * 根据源Id查询数据源信息
     *
     * @param source 数据源
     * @return 数据源
     */
    public Source mainSelectSourceBySourceId(Source source);

    /**
     * 新增数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainInsertSource(Source source);

    /**
     * 修改数据源
     *
     * @param source 数据源
     * @param ds           数据源新增|更新|删除判断
     * @return 结果
     */
    public int mainUpdateSource(Source source, int ds);

    /**
     * 修改数据源排序
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainUpdateSourceSort(Source source);

    /**
     * 删除数据源信息
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainDeleteSourceById(Source source);

    /**
     * 批量删除数据源
     *
     * @param DsIds 需停用的数据源集合
     * @return 结果
     */
    public int mainDeleteSourceByIds(List<Source> DsIds);

    /**
     * 校验数据源是否已应用于策略
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainCheckStrategySourceBySourceId(Source source);

    /**
     * 校验写数据源是否已设置主从配置
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainCheckSeparationSourceByWriteId(Source source);

    /**
     * 校验读数据源是否已应用于主从配置
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainCheckSeparationSourceByReadId(Source source);
}