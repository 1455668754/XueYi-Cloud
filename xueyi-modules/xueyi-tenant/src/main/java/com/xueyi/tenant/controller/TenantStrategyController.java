package com.xueyi.tenant.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.domain.TenantStrategy;
import com.xueyi.tenant.service.ITenantStrategyService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * 数据源策略 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/strategy")
public class TenantStrategyController extends BaseController {
    @Autowired
    private ITenantStrategyService tenantStrategyService;

    /**
     * 查询数据源策略列表
     */
    @PreAuthorize(hasPermi = "tenant:strategy:list")
    @GetMapping("/list")
    public TableDataInfo list(TenantStrategy tenantStrategy) {
        startPage();
        List<TenantStrategy> list = tenantStrategyService.selectTenantStrategyList(tenantStrategy);
        return getDataTable(list);
    }

    /**
     * 查询数据源策略列表（排除停用）
     */
    @GetMapping("/exclude")
    public AjaxResult exclude(TenantStrategy tenantStrategy) {
        return AjaxResult.success(tenantStrategyService.selectTenantStrategyListExclude(tenantStrategy));
    }

    /**
     * 导出数据源策略列表
     */
    @PreAuthorize(hasPermi = "tenant:strategy:export")
    @Log(title = "数据源策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TenantStrategy tenantStrategy) throws IOException {
        List<TenantStrategy> list = tenantStrategyService.selectTenantStrategyList(tenantStrategy);
        ExcelUtil<TenantStrategy> util = new ExcelUtil<TenantStrategy>(TenantStrategy.class);
        util.exportExcel(response, list, "数据源策略数据");
    }

    /**
     * 获取数据源策略详细信息
     */
    @PreAuthorize(hasPermi = "tenant:strategy:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(TenantStrategy tenantStrategy) {
        return AjaxResult.success(tenantStrategyService.selectTenantStrategyById(tenantStrategy));
    }

    /**
     * 新增数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:add")
    @Log(title = "数据源策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TenantStrategy tenantStrategy) {
        return toAjax(tenantStrategyService.insertTenantStrategy(tenantStrategy));
    }

    /**
     * 修改数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TenantStrategy tenantStrategy) {
        if (tenantStrategy.getIsChange()!= null && tenantStrategy.getIsChange() == 1) {
            return AjaxResult.error("禁止操作默认策略");
        }
        return toAjax(tenantStrategyService.updateTenantStrategy(tenantStrategy));
    }

    /**
     * 修改数据源策略排序
     */
    @PreAuthorize(hasPermi = "tenant:strategy:edit")
    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody TenantStrategy tenantStrategy) {
        return toAjax(tenantStrategyService.updateTenantStrategySort(tenantStrategy));
    }

    /**
     * 删除数据源策略
     */
    @PreAuthorize(hasPermi = "tenant:strategy:remove")
    @Log(title = "数据源策略", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody TenantStrategy tenantStrategy) {
        return toAjax(tenantStrategyService.deleteTenantStrategyByIds(tenantStrategy));
    }
}