package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.cache.service.ISysCacheInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 模块信息 Controller
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/system")
public class SysSystemController extends BaseController {

    @Autowired
    private ISysSystemService systemService;

    @Autowired
    private ISysCacheInitService cacheInitService;

    /**
     * 查询模块信息列表
     */
    @RequiresPermissions("system:system:list")
    @GetMapping("/list")
    public TableDataInfo list(SysSystem system) {
        startPage();
        List<SysSystem> list = systemService.mainSelectSystemList(system);
        return getDataTable(list);
    }

    /**
     * 获取模块信息详细信息
     */
    @RequiresPermissions("system:system:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysSystem system) {
        return AjaxResult.success(systemService.mainSelectSystemById(system));
    }

    /**
     * 新增模块信息
     */
    @RequiresPermissions("system:system:add")
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSystem system) {
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, system.getIsCommon()) && !SecurityUtils.isAdminTenant()) {
            return AjaxResult.error("新增模块'" + system.getName() + "'失败，仅租管账户可新增公共模块");
        }
        return toAjax(refreshCache(system, systemService.mainInsertSystem(system), true));
    }

    /**
     * 修改模块信息
     */
    @RequiresPermissions("system:system:edit")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSystem system) {
        SysSystem check = systemService.mainSelectSystemById(new SysSystem(system.getSystemId()));
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, check.getIsCommon()) && !SecurityUtils.isAdminTenant()) {
            return AjaxResult.error("修改模块'" + system.getName() + "'失败，仅租管账户可修改公共模块");
        }
        return toAjax(refreshCache(system, systemService.mainUpdateSystem(system), true));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:system:edit")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysSystem system) {
        SysSystem check = systemService.mainSelectSystemById(new SysSystem(system.getSystemId()));
        if (StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, check.getIsCommon()) && !SecurityUtils.isAdminTenant()) {
            return AjaxResult.error("修改模块状态'" + check.getName() + "'失败，仅租管账户可修改公共模块状态");
        }
        return toAjax(refreshCache(system, systemService.mainUpdateSystemStatus(system), true));
    }

    /**
     * 删除模块信息
     */
    @RequiresPermissions("system:system:remove")
    @Log(title = "模块管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysSystem system) {
        Set<SysSystem> before = systemService.mainCheckSystemListByIds(system);
        int rows = systemService.mainDeleteSystemByIds(system);
        if (rows > 0) {
            Set<SysSystem> after = systemService.mainCheckSystemListByIds(system);
            before.removeAll(after);
            for (SysSystem vo : before) {
                AuthorityUtils.deleteRouteCache(StringUtils.equals(AuthorityConstants.IS_COMMON_TRUE, vo.getIsCommon()) ? AuthorityConstants.COMMON_ENTERPRISE : SecurityUtils.getEnterpriseId(), vo.getSystemId());
            }
            refreshCache(system, rows, false);
        }
        return toAjax(rows);
    }

    /**
     * 查询首页可展示模块信息列表
     */
    @GetMapping("/getSystemRoutes")
    public AjaxResult getSystemRoutes() {
        return AjaxResult.success(systemService.getSystemRoutes());
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
                cacheInitService.refreshRouteCacheBySystemId(new SysSystem(system.getSnowflakeId(), SecurityUtils.getEnterpriseId()));
            }
            if (SecurityUtils.isAdminTenant()) {
                cacheInitService.refreshSystemCacheByEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
                cacheInitService.refreshSystemMenuCacheByEnterpriseId(AuthorityConstants.COMMON_ENTERPRISE);
            }
            cacheInitService.refreshSystemCacheByEnterpriseId(SecurityUtils.getEnterpriseId());
            cacheInitService.refreshSystemMenuCacheByEnterpriseId(SecurityUtils.getEnterpriseId());
        }
        return rows;
    }
}