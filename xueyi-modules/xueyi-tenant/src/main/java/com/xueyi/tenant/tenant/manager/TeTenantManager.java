package com.xueyi.tenant.tenant.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import org.springframework.stereotype.Component;

/**
 * 租户管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class TeTenantManager extends BaseManager<TeTenantDto, TeTenantMapper> {

    /**
     * 校验数据源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果
     */
    public TeTenantDto checkStrategyExist(Long strategyId) {
        return baseMapper.selectOne(
                Wrappers.<TeTenantDto>query().lambda()
                        .eq(TeTenantDto::getStrategyId, strategyId)
                        .last(SqlConstants.LIMIT_ONE));
    }
}