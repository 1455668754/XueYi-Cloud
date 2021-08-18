package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.datascope.annotation.DataScope;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.mapper.TenantStrategyMapper;
import com.xueyi.tenant.domain.TenantStrategy;
import com.xueyi.tenant.service.ITenantStrategyService;

/**
 * 数据源策略 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TenantStrategyServiceImpl implements ITenantStrategyService {

    @Autowired
    private TenantStrategyMapper tenantStrategyMapper;

    /**
     * 查询数据源策略列表
     *
     * @param tenantStrategy 数据源策略
     * @return 数据源策略
     */
    @Override
    public List<TenantStrategy> selectTenantStrategyList(TenantStrategy tenantStrategy) {
        return tenantStrategyMapper.selectTenantStrategyList(tenantStrategy);
    }

    /**
     * 查询数据源策略列表（排除停用）
     *
     * @param tenantStrategy 数据源策略
     * @return 数据源策略集合
     */
    @Override
    public List<TenantStrategy> selectTenantStrategyListExclude(TenantStrategy tenantStrategy) {
        return tenantStrategyMapper.selectTenantStrategyListExclude(tenantStrategy);
    }

    /**
     * 查询数据源策略
     *
     * @param tenantStrategy 数据源策略
     * @return 数据源策略
     */
    @Override
    public TenantStrategy selectTenantStrategyById(TenantStrategy tenantStrategy) {
        return tenantStrategyMapper.selectTenantStrategyById(tenantStrategy);
    }

    /**
     * 新增数据源策略
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertTenantStrategy(TenantStrategy tenantStrategy) {
        int rows = tenantStrategyMapper.insertTenantStrategy(tenantStrategy);
        if (tenantStrategy.getValues() != null && tenantStrategy.getValues().size() > 0) {
            /**获取生成雪花Id，并赋值给主键，加入至子表对应外键中*/
            tenantStrategy.setStrategyId(tenantStrategy.getSnowflakeId());
            tenantStrategyMapper.batchTenantSource(tenantStrategy);
        }
        return rows;
    }

    /**
     * 修改数据源策略
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTenantStrategy(TenantStrategy tenantStrategy) {
        if (!StringUtils.equals(UserConstants.STATUS_UPDATE_OPERATION, tenantStrategy.getUpdateType())) {
            tenantStrategyMapper.deleteTenantSourceByStrategyId(tenantStrategy);
            if (tenantStrategy.getValues() != null && tenantStrategy.getValues().size() > 0) {
                tenantStrategyMapper.batchTenantSource(tenantStrategy);
            }
        }
        return tenantStrategyMapper.updateTenantStrategy(tenantStrategy);
    }

    /**
     * 修改数据源策略排序
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @Override
    public int updateTenantStrategySort(TenantStrategy tenantStrategy) {
        return tenantStrategyMapper.updateTenantStrategySort(tenantStrategy);
    }

    /**
     * 删除数据源策略信息
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantStrategyById(TenantStrategy tenantStrategy) {
        tenantStrategyMapper.deleteTenantSourceByStrategyId(tenantStrategy);
        return tenantStrategyMapper.deleteTenantStrategyById(tenantStrategy);
    }

    /**
     * 批量删除数据源策略
     *
     * @param tenantStrategy 数据源策略
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantStrategyByIds(TenantStrategy tenantStrategy) {
        tenantStrategyMapper.deleteTenantSourceByStrategyIds(tenantStrategy);
        return tenantStrategyMapper.deleteTenantStrategyByIds(tenantStrategy);
    }
}