package com.xueyi.system.cache.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.cache.ISysLoadingCache;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.source.service.IDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 缓存加载 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("main")
public class SysLoadingCacheImpl implements ISysLoadingCache {

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private IDataSourceService dataSourceService;

    /**
     * 项目启动时，初始化缓存
     */
    @PostConstruct
    public void init() {
        List<SysEnterprise> enterprisesList = enterpriseService.mainSelectEnterpriseCacheList();
        /* 初始化企业信息缓存 */
        enterpriseService.loadingEnterpriseCache(enterprisesList);
        /* 初始化数据源策略组缓存 */
        dataSourceService.loadingSourceCache();
    }
}