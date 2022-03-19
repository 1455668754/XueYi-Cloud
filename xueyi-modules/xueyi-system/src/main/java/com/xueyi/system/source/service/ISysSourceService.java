package com.xueyi.system.source.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.source.domain.Source;

/**
 * 策略组管理 服务层
 *
 * @author xueyi
 */
public interface ISysSourceService extends IBaseService<Source> {

    /**
     * 加载策略组缓存数据
     */
    void loadingSourceCache();

    /**
     * 清空策略组缓存数据
     */
    void clearSourceCache();

    /**
     * 重置策略组缓存数据
     */
    void resetSourceCache();

    /**
     * 根据Id更新策略组缓存数据
     */
    void refreshSourceCache(Long id);
}
