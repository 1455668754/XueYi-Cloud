package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.SortUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysSystemMapper;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.cache.service.ISysCacheInitService;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 模块信息Service业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SysSystemServiceImpl implements ISysSystemService {

    @Autowired
    private ISysAuthorityService authorityService;

    @Autowired
    private ISysCacheInitService cacheInitService;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysSystemMapper systemMapper;

    @Autowired
    private ISysRoleSystemMenuService roleSystemMenuService;

    /**
     * 当前用户首页可展示的模块列表
     *
     * @return 模块信息集合
     */
    @Override
    public List<SysSystem> getSystemRoutes() {
        Set<SysSystem> systemSet = AuthorityUtils.getSystemCache(SecurityUtils.getEnterpriseId());
        Set<SysSystem> rangeSet;
        // 管理员显示所有模块信息
        if (SecurityUtils.isAdminUser()) {
            rangeSet = authorityService.selectSystemSet(SecurityUtils.getEnterpriseId(),authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false);
        } else {
            SysRole role = new SysRole();
            role.getParams().put("userId", SecurityUtils.getUserId());
            rangeSet = authorityService.selectSystemSet(SecurityUtils.getEnterpriseId(), authorityService.selectRoleListByUserId(role), SecurityUtils.isAdminTenant(), true);
        }
        systemSet.retainAll(rangeSet);
        return SortUtils.sortSetToList(systemSet);
    }

    /**
     * 查询模块信息列表
     *
     * @param system 模块信息
     * @return 模块信息集合
     */
    @Override
    public List<SysSystem> mainSelectSystemList(SysSystem system) {
        return systemMapper.mainSelectSystemList(system);
    }

    /**
     * 查询模块信息
     *
     * @param system 模块信息 | systemId 模块Id
     * @return 模块信息
     */
    @Override
    public SysSystem mainSelectSystemById(SysSystem system) {
        return systemMapper.mainSelectSystemById(system);
    }

    /**
     * 新增模块信息
     *
     * @param system 模块信息
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainInsertSystem(SysSystem system) {
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, system.getIsCommon()) && SecurityUtils.isAdminTenant()) {
            system.setEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
        }
        return refreshCache(system, systemMapper.mainInsertSystem(system), true);
    }

    /**
     * 修改模块信息
     *
     * @param system 模块信息
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainUpdateSystem(SysSystem system) {
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, system.getIsCommon()) && SecurityUtils.isAdminTenant()) {
            system.setEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
        }
        return refreshCache(system, systemMapper.mainUpdateSystem(system), true);
    }

    /**
     * 修改模块信息状态
     *
     * @param system 模块信息
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainUpdateSystemStatus(SysSystem system) {
        return refreshCache(system, systemMapper.mainUpdateSystemStatus(system), true);
    }

    /**
     * 批量删除模块信息
     *
     * @param system 模块信息 | params.Ids 需要删除的模块信息Ids组
     * @return 结果
     */
    @Override
    @DataScope(uedAlias = "empty")
    public int mainDeleteSystemByIds(SysSystem system) {
        Set<SysSystem> before = systemMapper.mainCheckSystemListByIds(system);
        int rows = systemMapper.mainDeleteSystemByIds(system, SecurityUtils.isAdminTenant());
        if (rows > 0) {
            Set<SysSystem> after = systemMapper.mainCheckSystemListByIds(system);
            before.removeAll(after);
            if (before.size() > 0) {
                for (SysSystem vo : before) {
                    AuthorityUtils.deleteRouteCache(StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, vo.getIsCommon()) ? AuthorityConstants.COMMON_ENTERPRISE : SecurityUtils.getEnterpriseId(), vo.getSystemId());
                }
                refreshCache(system, rows, false);
            }
        }
        return rows;
    }

    /**
     * 更新模块信息缓存
     *
     * @param system 模块信息
     * @param rows   结果
     * @param type   True更新 | False删除
     */
    private int refreshCache(SysSystem system, int rows, boolean type) {
        if (rows > 0) {
            if (type) {
                cacheInitService.refreshRouteCacheBySystem(new SysSystem(system.getSnowflakeId(), SecurityUtils.getEnterpriseId()));
            }
            if (SecurityUtils.isAdminTenant()) {
                cacheInitService.refreshSystemCacheBySystem(new SysSystem(system.getSnowflakeId(), AuthorityConstants.COMMON_ENTERPRISE));
                cacheInitService.refreshSystemMenuCacheBySystem(new SysSystem(system.getSnowflakeId(), AuthorityConstants.COMMON_ENTERPRISE));
            }
            cacheInitService.refreshSystemCacheBySystem(new SysSystem(system.getSnowflakeId(), SecurityUtils.getEnterpriseId()));
            cacheInitService.refreshSystemMenuCacheBySystem(new SysSystem(system.getSnowflakeId(), SecurityUtils.getEnterpriseId()));
        }
        return rows;
    }
}
