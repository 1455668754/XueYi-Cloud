package com.xueyi.system.cache.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.cache.domain.CacheInitVo;
import com.xueyi.system.cache.mapper.SysCacheInitMapper;
import com.xueyi.system.cache.service.ISysCacheInitService;
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
public class SysCacheInitServiceImpl implements ISysCacheInitService {

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private SysCacheInitMapper cacheInitMapper;

    @Autowired
    private ISysCacheInitService cacheInitService;

    /**
     * 项目启动时，初始化缓存
     */
    @PostConstruct
    public void init() {
        List<SysEnterprise> enterprisesList = enterpriseService.mainSelectEnterpriseCacheList();
        List<Source> sources = DataSourceUtils.getSourceList();
        /* 初始化企业信息缓存 */
        enterpriseService.loadingEnterpriseCache(enterprisesList);
        /* 初始化数据源策略组缓存 */
        dataSourceService.loadingSourceCache();
        /* 初始化模块-路由缓存 */
        loadingRouteCache();
        /* 初始化模块缓存 */
        loadingSystemCache();
        /* 初始化模块-菜单缓存 */
        loadingSystemMenuCache();
        /* 初始化模块-菜单缓存 */
        for (Source source : sources) {
            cacheInitService.loadingRoleCache(source.getMaster());
        }
    }

    /**
     * 加载模块-路由缓存数据
     */
    @Override
    public void loadingRouteCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectRouteCacheList();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshRouteCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemId(), cacheInitVo.getRouteSet());
        }
    }

    /**
     * 加载模块缓存数据
     */
    @Override
    public void loadingSystemCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectSystemCacheList();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemSet());
        }
    }

    /**
     * 加载模块-菜单缓存数据
     */
    @Override
    public void loadingSystemMenuCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectSystemMenuCacheList();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemMenuSet());
        }
    }

    /**
     * 加载角色缓存数据
     *
     * @param sourceName 数据源名称
     */
    @Override
    @DS("#sourceName")
    public void loadingRoleCache(String sourceName) {
        List<SysRole> roles = cacheInitMapper.mainSelectRoleCacheList();
        for (SysRole role : roles) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), role);
        }
    }
}