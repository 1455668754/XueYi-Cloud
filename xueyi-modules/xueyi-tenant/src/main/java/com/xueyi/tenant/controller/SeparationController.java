package com.xueyi.tenant.controller;

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
import com.xueyi.tenant.service.ISeparationService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;

/**
 * 数据源 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/separation")
public class SeparationController extends BaseController {

    @Autowired
    private ISeparationService separationService;

    /**
     * 查询 只读 数据源集合
     */
    @GetMapping("/containRead")
    public AjaxResult containRead(Source source) {
        return AjaxResult.success(separationService.mainSelectContainReadList(source));
    }

    /**
     * 查询 含写 数据源集合
     */
    @GetMapping("/containWrite")
    public AjaxResult containWrite(Source source) {
        return AjaxResult.success(separationService.mainSelectContainWriteList(source));
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize(hasPermi = "tenant:separation:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Source source) {
        return AjaxResult.success(separationService.selectTenantSeparationById(source));
    }

    /**
     * 修改数据源
     */
    @PreAuthorize(hasPermi = "tenant:separation:edit")
    @Log(title = "数据源读写分离", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Source source) {
        return toAjax(separationService.updateTenantSeparation(source));
    }
}