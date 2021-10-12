package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.organize.SysEnterprise;

import java.util.List;

/**
 * 企业缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysEnterpriseCacheMapper {

    /**
     * 查询所有企业信息 | 主源所有企业
     *
     * @return 企业对象集合
     */
    public List<SysEnterprise> mainSelectEnterpriseCacheListBySource();
}
