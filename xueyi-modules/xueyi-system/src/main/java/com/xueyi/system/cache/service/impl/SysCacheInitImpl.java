package com.xueyi.system.cache.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.cache.domain.CacheInitVo;
import com.xueyi.system.cache.mapper.SysCacheInitMapper;
import com.xueyi.system.cache.service.ISysCacheInit;
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
public class SysCacheInitImpl implements ISysCacheInit {

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private SysCacheInitMapper cacheInitMapper;

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
        /* 初始化菜单缓存 */
        loadingMenuCache();
        /* 初始化模块缓存 */
        loadingSystemCache();
    }

    /**
     * 加载菜单缓存数据
     */
    @Override
    public void loadingMenuCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectMenuCacheList();
        for (CacheInitVo cacheInitVo: cacheInitVos) {
            AuthorityUtils.refreshMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getMenuSet());
        }
    }

    /**
     * 加载模块缓存数据
     */
    @Override
    public void loadingSystemCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectSystemCacheList();
        for (CacheInitVo cacheInitVo: cacheInitVos) {
            AuthorityUtils.refreshSystemCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemSet());
        }
    }
}