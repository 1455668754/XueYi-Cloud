package com.xueyi.tenant.controller;

import java.util.List;

import com.xueyi.tenant.api.domain.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.service.ITenantSeparationService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * 数据源 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/separation")
public class TenantSeparationController extends BaseController {

    @Autowired
    private ITenantSeparationService tenantSeparationService;

    /**
     * 查询数据源列表
     */
    @PreAuthorize(hasPermi = "tenant:separation:list")
    @GetMapping("/list")
    public TableDataInfo list(Source source) {
        startPage();
        List<Source> list = tenantSeparationService.mainSelectSeparationList(source);
        return getDataTable(list);
    }

    /**
     * 查询 只读 数据源集合
     */
    @GetMapping("/containRead")
    public AjaxResult containRead(Source source) {
        return AjaxResult.success(tenantSeparationService.selectContainReadList(source));
    }

    /**
     * 查询 含写 数据源集合
     */
    @GetMapping("/containWrite")
    public AjaxResult containWrite(Source source) {
        return AjaxResult.success(tenantSeparationService.selectContainWriteList(source));
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize(hasPermi = "tenant:separation:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Source source) {
        return AjaxResult.success(tenantSeparationService.selectTenantSeparationById(source));
    }

    /**
     * 修改数据源
     */
    @PreAuthorize(hasPermi = "tenant:separation:edit")
    @Log(title = "数据源读写分离", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Source source) {
        return toAjax(tenantSeparationService.updateTenantSeparation(source));
    }
}