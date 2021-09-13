package com.xueyi.tenant.mapper;

import java.util.List;

import com.xueyi.tenant.domain.Strategy;

/**
 * 数据源策略 数据层
 *
 * @author xueyi
 */
public interface StrategyMapper {
    /**
     * 查询数据源策略列表
     *
     * @param strategy 数据源策略
     * @return 数据源策略集合
     */
    public List<Strategy> mainSelectStrategyList(Strategy strategy);

    /**
     * 查询数据源策略列表（排除停用）
     *
     * @param strategy 数据源策略
     * @return 数据源策略集合
     */
    public List<Strategy> mainSelectStrategyListExclude(Strategy strategy);

    /**
     * 查询数据源策略
     *
     * @param strategy 数据源策略 | strategyId 策略Id
     * @return 数据源策略
     */
    public Strategy mainSelectStrategyById(Strategy strategy);

    /**
     * 新增数据源策略
     * 访问控制 empty 租户更新（无前缀）()控制方法在impl层 | TenantStrategyServiceImpl)
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainInsertStrategy(Strategy strategy);

    /**
     * 修改数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainUpdateStrategy(Strategy strategy);

    /**
     * 修改数据源策略排序
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainUpdateStrategySort(Strategy strategy);

    /**
     * 删除数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainDeleteStrategyById(Strategy strategy);

    /**
     * 批量删除数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainDeleteStrategyByIds(Strategy strategy);

    /**
     * 批量新增数据源
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainBatchSource(Strategy strategy);

    /**
     * 通过数据源策略Id删除数据源信息
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainDeleteSourceByStrategyId(Strategy strategy);

    /**
     * 批量删除数据源
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    public int mainDeleteSourceByStrategyIds(Strategy strategy);
}