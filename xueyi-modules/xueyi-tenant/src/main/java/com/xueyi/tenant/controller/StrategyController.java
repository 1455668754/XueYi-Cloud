package com.xueyi.tenant.controller;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.ParamsUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.feign.RemoteSourceService;
import com.xueyi.tenant.api.domain.strategy.Strategy;
import com.xueyi.tenant.service.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源策略 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/strategy")
public class StrategyController extends BaseController {

    @Autowired
    private IStrategyService tenantStrategyService;

    @Autowired
    private RemoteSourceService remoteSourceService;

    /**
     * 查询数据源策略列表
     */
    @RequiresPermissions("tenant:strategy:list")
    @GetMapping("/list")
    public TableDataInfo list(Strategy strategy) {
        startPage();
        List<Strategy> list = tenantStrategyService.mainSelectStrategyList(strategy);
        return getDataTable(list);
    }

    /**
     * 查询数据源策略列表（排除停用）
     */
    @GetMapping("/exclude")
    public AjaxResult exclude(Strategy strategy) {
        return AjaxResult.success(tenantStrategyService.mainSelectStrategyListExclude(strategy));
    }

    /**
     * 获取数据源策略详细信息
     */
    @RequiresPermissions("tenant:strategy:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Strategy strategy) {
        return AjaxResult.success(tenantStrategyService.mainSelectStrategyById(strategy));
    }

    /**
     * 新增数据源策略
     */
    @RequiresPermissions("tenant:strategy:add")
    @Log(title = "数据源策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Strategy strategy) {
        int rows = tenantStrategyService.mainInsertStrategy(strategy);
        if (rows > 0 && StringUtils.equals(TenantConstants.NORMAL, strategy.getStatus())) {
            remoteSourceService.refreshSource(strategy.getStrategyId(), SecurityConstants.INNER);
        }
        return toAjax(rows);
    }

    /**
     * 修改数据源策略
     */
    @RequiresPermissions("tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Strategy strategy) {
        if (StringUtils.equals(strategy.getIsChange(), Constants.SYSTEM_DEFAULT_TRUE)) {
            return AjaxResult.error("禁止操作默认策略");
        }
        int rows = tenantStrategyService.mainUpdateStrategy(strategy);
        remoteSourceService.refreshSource(strategy.getStrategyId(), SecurityConstants.INNER);
        return toAjax(rows);
    }

    /**
     * 修改数据源策略排序
     */
    @RequiresPermissions("tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Strategy strategy) {
        return toAjax(tenantStrategyService.mainUpdateStrategySort(strategy));
    }

    /**
     * 删除数据源策略
     */
    @RequiresPermissions("tenant:strategy:remove")
    @Log(title = "数据源策略", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Strategy strategy) {
        int rows = tenantStrategyService.mainDeleteStrategyByIds(strategy);
        DataSourceUtils.deleteSourceCaches(ParamsUtils.IdsObjectToLongList(strategy.getParams().get("Ids")));
        return toAjax(rows);
    }
}