package com.xueyi.system.cache.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.cache.domain.CacheInitVo;
import com.xueyi.system.cache.mapper.SysAuthorityCacheMapper;
import com.xueyi.system.cache.mapper.SysEnterpriseCacheMapper;
import com.xueyi.system.cache.mapper.SysRoleCacheMapper;
import com.xueyi.system.cache.mapper.SysSourceCacheMapper;
import com.xueyi.system.cache.service.ISysCacheInitService;
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
    private SysEnterpriseCacheMapper enterpriseCacheMapper;

    @Autowired
    private SysSourceCacheMapper sourceCacheMapper;

    @Autowired
    private SysAuthorityCacheMapper authorityCacheMapper;

    @Autowired
    private SysRoleCacheMapper roleCacheMapper;

    @Autowired
    private ISysCacheInitService cacheInitService;

    /**
     * 项目启动时，初始化缓存
     */
    @PostConstruct
    public void init() {
        /* 初始化企业信息缓存 */
        cacheInitService.loadingEnterpriseCache();
        /* 初始化数据源策略组缓存 */
        cacheInitService.loadingSourceCache();
        /* 初始化模块-路由缓存 */
        cacheInitService.loadingRouteCache();
        /* 初始化菜单缓存 */
        cacheInitService.loadingMenuCache();
        /* 初始化模块缓存 */
        cacheInitService.loadingSystemCache();
        /* 初始化模块-菜单缓存 */
        cacheInitService.loadingSystemMenuCache();
        /* 初始化模块-角色缓存 */
        List<Source> sources = DataSourceUtils.getSourceList();
        for (Source source : sources) {
            cacheInitService.loadingRoleCache(source.getMaster());
        }
    }

    /**
     * 加载指定企业全部缓存数据 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void loadingEnterpriseAllCacheByEnterpriseId(Long enterpriseId) {
        /* 初始化企业信息缓存 */
        cacheInitService.refreshEnterpriseCacheByEnterpriseId(enterpriseId);
        /* 初始化模块-路由缓存 */
        cacheInitService.refreshRouteCacheByEnterpriseId(enterpriseId);
        /* 初始化菜单缓存 */
        cacheInitService.refreshMenuCacheByEnterpriseId(enterpriseId);
        /* 初始化模块缓存 */
        cacheInitService.refreshSystemCacheByEnterpriseId(enterpriseId);
        /* 初始化模块-菜单缓存 */
        cacheInitService.refreshSystemMenuCacheByEnterpriseId(enterpriseId);
        /* 初始化模块-角色缓存 */
        cacheInitService.refreshRoleCacheByEnterpriseIdToSourceName(enterpriseId, DataSourceUtils.getMainSourceNameByEnterpriseId(enterpriseId));
    }

    /**
     * 加载企业缓存数据 | 主源所有企业
     */
    @Override
    public void loadingEnterpriseCache() {
        List<SysEnterprise> enterpriseList = enterpriseCacheMapper.mainSelectEnterpriseCacheListBySource();
        for (SysEnterprise enterprise : enterpriseList) {
            EnterpriseUtils.refreshEnterpriseCache(enterprise.getEnterpriseId(), enterprise);
            EnterpriseUtils.refreshStrategyCache(enterprise.getEnterpriseId(), enterprise.getStrategyId());
            EnterpriseUtils.refreshLoginCache(enterprise.getEnterpriseName(), enterprise.getEnterpriseId());
        }
    }

    /**
     * 加载指定企业缓存数据 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void refreshEnterpriseCacheByEnterpriseId(Long enterpriseId) {
        SysEnterprise enterprise = enterpriseCacheMapper.mainSelectEnterpriseCacheByEnterpriseId(enterpriseId);
        if (enterprise != null) {
            EnterpriseUtils.refreshEnterpriseCache(enterprise.getEnterpriseId(), enterprise);
            EnterpriseUtils.refreshStrategyCache(enterprise.getEnterpriseId(), enterprise.getStrategyId());
            EnterpriseUtils.refreshLoginCache(enterprise.getEnterpriseName(), enterprise.getEnterpriseId());
        }
    }

    /**
     * 加载数据源策略组缓存数据 | 主源所有数据源策略组
     */
    @Override
    public void loadingSourceCache() {
        List<Source> sourceList = sourceCacheMapper.mainSelectSourceCacheListBySource();
        for (Source source : sourceList) {
            for (Source value : source.getValues()) {
                if (StringUtils.equals(TenantConstants.IS_MAIN_TRUE, value.getIsMain())) {
                    source.setMaster(value.getMaster());
                    source.setSlave(value.getSlave());
                }
            }
            DataSourceUtils.refreshSourceCache(source.getStrategyId(), source);
        }
    }

    /**
     * 加载数据源策略组缓存数据 | 主源单个指定数据源策略组
     *
     * @param strategyId 源策略组Id
     */
    @Override
    public void refreshSourceCacheByStrategyId(Long strategyId) {
        Source source = sourceCacheMapper.mainSelectSourceCacheByStrategyId(strategyId);
        for (Source value : source.getValues()) {
            if (StringUtils.equals(TenantConstants.IS_MAIN_TRUE, value.getIsMain())) {
                source.setMaster(value.getMaster());
                source.setSlave(value.getSlave());
            }
        }
        DataSourceUtils.refreshSourceCache(source.getStrategyId(), source);
    }

    /**
     * 加载模块-路由缓存数据 | 主源所有企业
     */
    @Override
    public void loadingRouteCache() {
        List<CacheInitVo> cacheInitVos = authorityCacheMapper.mainSelectRouteCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshRouteCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemId(), cacheInitVo.getMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = authorityCacheMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), cacheInitVos.stream().map(CacheInitVo::getSystemId).collect(Collectors.toSet()));
        for (CacheInitVo enterpriseNull : enterpriseNullList) {
            AuthorityUtils.deleteRouteCache(enterpriseNull.getEnterpriseId(), enterpriseNull.getSystemId());
        }
    }

    /**
     * 查询指定企业模块-路由信息 | 指定企业
     *
     * @param enterpriseId 企业Id
     */
    @Override
    public void refreshRouteCacheByEnterpriseId(Long enterpriseId) {
        List<CacheInitVo> cacheInitVos = authorityCacheMapper.mainSelectRouteCacheListByEnterpriseId(enterpriseId);
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshRouteCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemId(), cacheInitVo.getMenuSet());
        }
    }

    /**
     * 加载模块-路由缓存数据 | 单个企业的单个指定模块
     *
     * @param system 模块信息 | systemId 模块Id | enterpriseId 企业Id
     */
    @Override
    public void refreshRouteCacheBySystemId(SysSystem system) {
        CacheInitVo cacheInitVo = authorityCacheMapper.mainSelectRouteCacheListBySystemId(system);
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
        List<CacheInitVo> cacheInitVos = authorityCacheMapper.mainSelectMenuCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = authorityCacheMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
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
        CacheInitVo cacheInitVo = authorityCacheMapper.mainSelectMenuCacheListByEnterpriseId(enterpriseId);
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
        List<CacheInitVo> cacheInitVos = authorityCacheMapper.mainSelectSystemCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemSet());
        }
        List<CacheInitVo> enterpriseNullList = authorityCacheMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
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
        CacheInitVo cacheInitVo = authorityCacheMapper.mainSelectSystemCacheListByEnterpriseId(enterpriseId);
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
        List<CacheInitVo> cacheInitVos = authorityCacheMapper.mainSelectSystemMenuCacheListBySource();
        for (CacheInitVo cacheInitVo : cacheInitVos) {
            AuthorityUtils.refreshSystemMenuCache(cacheInitVo.getEnterpriseId(), cacheInitVo.getSystemMenuSet());
        }
        List<CacheInitVo> enterpriseNullList = authorityCacheMapper.mainSelectEnterpriseCacheListByExcludeIds(cacheInitVos.stream().map(CacheInitVo::getEnterpriseId).collect(Collectors.toSet()), null);
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
        CacheInitVo cacheInitVo = authorityCacheMapper.mainSelectSystemMenuCacheListByEnterpriseId(enterpriseId);
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
        List<SysRole> roles = roleCacheMapper.selectRoleCacheListBySource();
        for (SysRole role : roles) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), role);
        }
        List<SysRole> roleNullList = roleCacheMapper.selectRoleCacheListByExcludeIds(roles.stream().map(SysRole::getEnterpriseId).collect(Collectors.toSet()), roles.stream().map(SysRole::getRoleId).collect(Collectors.toSet()));
        for (SysRole roleNull : roleNullList) {
            AuthorityUtils.deleteRoleCache(roleNull.getEnterpriseId(), roleNull.getRoleId());
        }
    }

    /**
     * 加载指定企业的所有角色缓存数据 | 指定企业
     *
     * @param enterpriseId 租户Id
     * @param sourceName   指定源
     */
    @Override
    @DS("#sourceName")
    public void refreshRoleCacheByEnterpriseIdToSourceName(Long enterpriseId, String sourceName) {
        List<SysRole> roles = roleCacheMapper.selectRoleCacheListByEnterpriseId(enterpriseId);
        for (SysRole role : roles) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), role);
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
        SysRole roleCache = roleCacheMapper.selectRoleCacheByRoleId(role);
        if (roleCache != null) {
            AuthorityUtils.refreshRoleCache(role.getEnterpriseId(), role.getRoleId(), roleCache);
        } else {
            AuthorityUtils.deleteRoleCache(role.getEnterpriseId(), role.getRoleId());
        }
    }
}