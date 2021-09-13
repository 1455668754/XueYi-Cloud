package com.xueyi.tenant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.api.domain.strategy.Strategy;
import com.xueyi.tenant.service.IStrategyService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;

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

    /**
     * 查询数据源策略列表
     */
    @PreAuthorize(hasPermi = "tenant:strategy:list")
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
    @PreAuthorize(hasPermi = "tenant:strategy:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Strategy strategy) {
        return AjaxResult.success(tenantStrategyService.mainSelectStrategyById(strategy));
    }

    /**
     * 新增数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:add")
    @Log(title = "数据源策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Strategy strategy) {
        return toAjax(tenantStrategyService.mainInsertStrategy(strategy));
    }

    /**
     * 修改数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Strategy strategy) {
        if (strategy.getIsChange()!= null && strategy.getIsChange() == 1) {
            return AjaxResult.error("禁止操作默认策略");
        }
        return toAjax(tenantStrategyService.mainUpdateStrategy(strategy));
    }

    /**
     * 修改数据源策略排序
     */
    @PreAuthorize(hasPermi = "tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Strategy strategy) {
        return toAjax(tenantStrategyService.mainUpdateStrategySort(strategy));
    }

    /**
     * 删除数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:remove")
    @Log(title = "数据源策略", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Strategy strategy) {
        return toAjax(tenantStrategyService.mainDeleteStrategyByIds(strategy));
    }
}