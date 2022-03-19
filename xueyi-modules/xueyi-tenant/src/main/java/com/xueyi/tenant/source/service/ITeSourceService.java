package com.xueyi.tenant.source.service;

import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.common.web.entity.service.IBaseService;

/**
 * 数据源管理 服务层
 *
 * @author xueyi
 */
public interface ITeSourceService extends IBaseService<TeSourceDto> {

    /**
     * 校验数据源是否为默认数据源
     *
     * @param id 数据源id
     * @return 结果 | true/false 是/不是
     */
    boolean checkIsDefault(Long id);
}