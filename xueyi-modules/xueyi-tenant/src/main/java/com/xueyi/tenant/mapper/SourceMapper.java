package com.xueyi.tenant.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.api.domain.source.Source;

/**
 * 数据源 数据层
 *
 * @author xueyi
 */
public interface SourceMapper {

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
     * 新增数据源
     * 访问控制 empty 租户更新（无前缀） | 控制方法位于SourceServiceImpl mainInsertSource
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainInsertSource(Source source);

    /**
     * 修改数据源
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param source 数据源
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateSource(Source source);

    /**
     * 修改数据源排序
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainUpdateSourceSort(Source source);

    /**
     * 删除数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainDeleteSourceById(Source source);

    /**
     * 批量删除数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainDeleteTenantSeparationByValueId(Source source);

    /**
     * 批量新增写-读数据源
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainBatchSeparation(Source source);

    /**
     * 通过写数据源Id删除读数据源关联信息
     *
     * @param source 数据源
     * @return 结果
     */
    public int mainDeleteSeparationBySourceId(Source source);

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