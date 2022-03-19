package com.xueyi.tenant.tenant.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;

/**
 * 数据源策略管理 服务层
 *
 * @author xueyi
 */
public interface ITeStrategyService extends IBaseService<TeStrategyDto> {

    /**
     * 校验数据源是否被使用
     *
     * @param sourceId 数据源id
     * @return 结果 | true/false 存在/不存在
     */
    boolean checkSourceExist(Long sourceId);

    /**
     * 校验源策略是否为默认源策略
     *
     * @param id 源策略id
     * @return 结果 | true/false 是/不是
     */
    boolean checkIsDefault(Long id);
}