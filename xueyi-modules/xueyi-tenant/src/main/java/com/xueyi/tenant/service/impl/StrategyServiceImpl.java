package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.system.api.feign.RemoteSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.datascope.annotation.DataScope;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.mapper.StrategyMapper;
import com.xueyi.tenant.api.domain.strategy.Strategy;
import com.xueyi.tenant.service.IStrategyService;

/**
 * 数据源策略 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class StrategyServiceImpl implements IStrategyService {

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private RemoteSourceService remoteSourceService;

    /**
     * 查询数据源策略列表
     *
     * @param strategy 数据源策略
     * @return 数据源策略
     */
    @Override
    public List<Strategy> mainSelectStrategyList(Strategy strategy) {
        return strategyMapper.mainSelectStrategyList(strategy);
    }

    /**
     * 查询数据源策略列表（排除停用）
     *
     * @param strategy 数据源策略
     * @return 数据源策略集合
     */
    @Override
    public List<Strategy> mainSelectStrategyListExclude(Strategy strategy) {
        return strategyMapper.mainSelectStrategyListExclude(strategy);
    }

    /**
     * 查询数据源策略
     *
     * @param strategy 数据源策略
     * @return 数据源策略
     */
    @Override
    public Strategy mainSelectStrategyById(Strategy strategy) {
        return strategyMapper.mainSelectStrategyById(strategy);
    }

    /**
     * 新增数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int mainInsertStrategy(Strategy strategy) {
        int rows = strategyMapper.mainInsertStrategy(strategy);
        if (strategy.getValues() != null && strategy.getValues().size() > 0) {
            /**获取生成雪花Id，并赋值给主键，加入至子表对应外键中*/
            strategy.setStrategyId(strategy.getSnowflakeId());
            strategyMapper.mainBatchSource(strategy);
        }
        if (rows > 0 && StringUtils.equals(TenantConstants.NORMAL, strategy.getStatus())) {
            remoteSourceService.refreshSource(strategy.getStrategyId(), SecurityConstants.INNER);
        }
        return rows;
    }

    /**
     * 修改数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int mainUpdateStrategy(Strategy strategy) {
        if (!StringUtils.equals(UserConstants.STATUS_UPDATE_OPERATION, strategy.getUpdateType())) {
            strategyMapper.mainDeleteSourceByStrategyId(strategy);
            if (strategy.getValues() != null && strategy.getValues().size() > 0) {
                strategyMapper.mainBatchSource(strategy);
            }
        }
        int rows = strategyMapper.mainUpdateStrategy(strategy);
        remoteSourceService.refreshSource(strategy.getStrategyId(), SecurityConstants.INNER);
        return rows;
    }

    /**
     * 修改数据源策略排序
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    @Override
    public int mainUpdateStrategySort(Strategy strategy) {
        return strategyMapper.mainUpdateStrategySort(strategy);
    }

    /**
     * 删除数据源策略信息
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int mainDeleteStrategyById(Strategy strategy) {
        strategyMapper.mainDeleteSourceByStrategyId(strategy);
        int rows = strategyMapper.mainDeleteStrategyById(strategy);
        DataSourceUtils.deleteCache(strategy.getStrategyId());
        return rows;
    }

    /**
     * 批量删除数据源策略
     *
     * @param strategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int mainDeleteStrategyByIds(Strategy strategy) {
//        strategyMapper.mainDeleteSourceByStrategyIds(strategy);
//        int rows = strategyMapper.mainDeleteStrategyByIds(strategy);
        DataSourceUtils.deleteCaches((List<Long>)strategy.getParams().get("Ids"));
        return 1;
    }
}