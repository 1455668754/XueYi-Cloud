package com.xueyi.system.cache.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.cache.domain.CacheInitVo;
import com.xueyi.system.cache.mapper.SysCacheInitMapper;
import com.xueyi.system.cache.service.ISysCacheInitService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

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
    private SysCacheInitMapper cacheInitMapper;

    @Autowired
    private ISysCacheInitService cacheInitService;

    /**
     * 项目启动时，初始化缓存
     */
    @PostConstruct
    public void init() {
        List<SysEnterprise> enterprisesList = enterpriseService.mainSelectEnterpriseCacheList();
        /* 初始化企业信息缓存 */
        enterpriseService.loadingEnterpriseCache(enterprisesList);
        /* 初始化数据源策略组缓存 */
        loadingSourceCache();
        /* 初始化模块-路由缓存 */
        loadingRouteCache();
        /* 初始化菜单缓存 */
        loadingMenuCache();
        /* 初始化模块缓存 */
        loadingSystemCache();
        /* 初始化模块-菜单缓存 */
        loadingSystemMenuCache();
        /* 初始化模块-菜单缓存 */
        List<Source> sources = DataSourceUtils.getSourceList();
        for (Source source : sources) {
            cacheInitService.loadingRoleCache(source.getMaster());
        }
    }

    /**
     * 加载数据源策略组缓存数据 | 主源所有数据源策略组
     */
    @Override
    public void loadingSourceCache() {
        List<Source> sourceList = cacheInitMapper.mainSelectSourceCacheListBySource();
        for (Source source : sourceList) {
            for(Source value: source.getValues()){
                if(StringUtils.equals(TenantConstants.IS_MAIN_TRUE,value.getIsMain())){
                    source.setMaster(value.getMaster());
                    source.setSlave(value.getSlave());
                }
            }
            DataSourceUtils.refreshSourceCache(source.getStrategyId(),source);
        }
    }

    /**
     * 加载数据源策略组缓存数据 | 主源单个指定数据源策略组
     *
     * @param strategyId 源策略组Id
     */
    @Override
    public void refreshSourceCacheByStrategyId(Long strategyId) {
        Source source = cacheInitMapper.mainSelectSourceCacheByStrategyId(strategyId);
        for(Source value: source.getValues()){
            if(StringUtils.equals(TenantConstants.IS_MAIN_TRUE,value.getIsMain())){
                source.setMaster(value.getMaster());
                source.setSlave(value.getSlave());
            }
        }
        DataSourceUtils.refreshSourceCache(source.getStrategyId(),source);
    }

    /**
     * 加载模块-路由缓存数据 | 主源所有企业
     */
    @Override
    public void loadingRouteCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectRouteCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshRouteCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemId(), cacheInitVo.getMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = cacheInitMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), cacheInitVos.stream().map(CacheInitVo::getSystemId).collect(Collectors.toSet()));
        for (CacheInitVo enterpriseNull : enterpriseNullList) {
            AuthorityUtils.deleteRouteCache(enterpriseNull.getEnterpriseId(), enterpriseNull.getSystemId());
        }
    }

    /**
     * 加载模块-路由缓存数据 | 单个企业的单个指定模块
     *
     * @param system 模块信息 | systemId 模块Id | enterpriseId 企业Id
     */
    @Override
    public void refreshRouteCacheBySystemId(SysSystem system) {
        CacheInitVo cacheInitVo = cacheInitMapper.mainSelectRouteCacheListBySystemId(system);
        if (cacheInitVo != null) {
            AuthorityUtils.refreshRouteCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemId(), cacheInitVo.getMenuSet());
        } else {
            AuthorityUtils.deleteRouteCache(system.getEnterpriseId(), system.getSystemId());
        }
    }

    /**
     * 加载菜单缓存数据 | 主源所有企业
     */
    @Override
    public void loadingMenuCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectMenuCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = cacheInitMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
        for (CacheInitVo enterpriseNull : enterpriseNullList) {
            AuthorityUtils.deleteMenuCache(enterpriseNull.getEnterpriseId());
        }
    }

    /**
     * 加载菜单缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void refreshMenuCacheByEnterpriseId(Long enterpriseId) {
        CacheInitVo cacheInitVo = cacheInitMapper.mainSelectMenuCacheListByEnterpriseId(enterpriseId);
        if (cacheInitVo != null) {
            AuthorityUtils.refreshMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getMenuSet());
        } else {
            AuthorityUtils.deleteMenuCache(enterpriseId);
        }
    }

    /**
     * 加载模块缓存数据 | 主源所有企业
     */
    @Override
    public void loadingSystemCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectSystemCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemSet());
        }
        List<CacheInitVo> enterpriseNullList = cacheInitMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
        for (CacheInitVo enterpriseNull : enterpriseNullList) {
            AuthorityUtils.deleteSystemCache(enterpriseNull.getEnterpriseId());
        }
    }

    /**
     * 加载模块缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void refreshSystemCacheByEnterpriseId(Long enterpriseId) {
        CacheInitVo cacheInitVo = cacheInitMapper.mainSelectSystemCacheListByEnterpriseId(enterpriseId);
        if (cacheInitVo != null) {
            AuthorityUtils.refreshSystemCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemSet());
        } else {
            AuthorityUtils.deleteSystemCache(enterpriseId);
        }
    }

    /**
     * 加载模块-菜单缓存数据 | 主源所有企业
     */
    @Override
    public void loadingSystemMenuCache() {
        List<CacheInitVo> cacheInitVos = cacheInitMapper.mainSelectSystemMenuCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = cacheInitMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
        for (CacheInitVo enterpriseNull : enterpriseNullList) {
            AuthorityUtils.deleteSystemMenuCache(enterpriseNull.getEnterpriseId());
        }
    }

    /**
     * 加载模块-菜单缓存数据 | 单个指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void refreshSystemMenuCacheByEnterpriseId(Long enterpriseId) {
        CacheInitVo cacheInitVo = cacheInitMapper.mainSelectSystemMenuCacheListByEnterpriseId(enterpriseId);
        if (cacheInitVo != null) {
            AuthorityUtils.refreshSystemMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemMenuSet());
        } else {
            AuthorityUtils.deleteSystemMenuCache(enterpriseId);
        }
    }

    /**
     * 加载角色缓存数据 | 指定源所有企业
     *
     * @param sourceName 数据源名称
     */
    @Override
    @DS("#sourceName")
    public void loadingRoleCache(String sourceName) {
        List<SysRole> roles = cacheInitMapper.selectRoleCacheListBySource();
        for (SysRole role : roles) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), role);
        }
        List<SysRole> roleNullList = cacheInitMapper.selectRoleCacheListByExcludeIds(roles.stream().map(SysRole::getEnterpriseId).collect(Collectors.toSet()), roles.stream().map(SysRole::getRoleId).collect(Collectors.toSet()));
        for (SysRole roleNull : roleNullList) {
            AuthorityUtils.deleteRoleCache(roleNull.getEnterpriseId(), roleNull.getRoleId());
        }
    }

    /**
     * 加载角色缓存数据 | 单个企业的单个指定角色
     *
     * @param role       角色信息 | roleId 角色Id | enterpriseId 租户Id
     * @param sourceName 指定源
     */
    @Override
    @DS("#sourceName")
    public void refreshRoleCacheByRoleIdToSourceName(SysRole role, String sourceName) {
        refreshRoleCacheByRoleId(role);
    }

    /**
     * 加载角色缓存数据 | 单个企业的单个指定角色
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    @Override
    @DS("#isolate")
    @DataScope(eAlias = "r")
    public void refreshRoleCacheByRoleIdToIsolate(SysRole role) {
        refreshRoleCacheByRoleId(role);
    }

    /**
     * 加载角色缓存数据 | 单个企业的单个指定角色
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    private void refreshRoleCacheByRoleId(SysRole role) {
        SysRole roleCache = cacheInitMapper.selectRoleCacheByRoleId(role);
        if (roleCache != null) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), roleCache);
        } else {
            AuthorityUtils.deleteRoleCache(role.getEnterpriseId(), role.getRoleId());
        }
    }
}