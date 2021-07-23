package com.xueyi.tenant.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.tenant.api.domain.source.TenantSource;
import com.xueyi.tenant.service.ITenantSeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.service.ITenantSourceService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * 数据源 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class TenantSourceController extends BaseController {

    @Autowired
    private ITenantSourceService tenantSourceService;

    @Autowired
    private ITenantSeparationService tenantSeparationService;

    /**
     * 查询数据源列表
     */
    @PreAuthorize(hasPermi = "tenant:source:list")
    @GetMapping("/list")
    public TableDataInfo list(TenantSource tenantSource) {
        startPage();
        List<TenantSource> list = tenantSourceService.selectTenantSourceList(tenantSource);
        return getDataTable(list);
    }

    /**
     * 导出数据源列表
     */
    @PreAuthorize(hasPermi = "tenant:source:export")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TenantSource tenantSource) throws IOException {
        List<TenantSource> list = tenantSourceService.selectTenantSourceList(tenantSource);
        ExcelUtil<TenantSource> util = new ExcelUtil<TenantSource>(TenantSource.class);
        util.exportExcel(response, list, "数据源数据");
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize(hasPermi = "tenant:source:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(TenantSource tenantSource) {
        return AjaxResult.success(tenantSourceService.selectTenantSourceById(tenantSource));
    }

    /**
     * 新增数据源
     */
    @PreAuthorize(hasPermi = "tenant:source:add")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TenantSource tenantSource) {
        return toAjax(tenantSourceService.insertTenantSource(tenantSource));
    }

    /**
     * 修改数据源
     */
    @PreAuthorize(hasPermi = "tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TenantSource tenantSource) {
        boolean key;
        if (StringUtils.isNotNull(tenantSource.getName())) {
            key = true;
        }else {
            key = false;
        }
        int update;
        TenantSource check = new TenantSource();
        check.setSourceId(tenantSource.getSourceId());
        TenantSource oldSource = tenantSourceService.selectTenantSourceById(check);
        if (StringUtils.equals(TenantConstants.SOURCE_WRITE, tenantSource.getType()) && StringUtils.equals(TenantConstants.NORMAL, tenantSource.getStatus())) {
            if (!StringUtils.equals(oldSource.getStatus(), tenantSource.getStatus()) && tenantSourceService.checkSeparationSourceByWriteId(check) == 0) {
                tenantSource.setStatus(oldSource.getStatus());
                if (key) {
                    update = tenantSourceService.updateTenantSource(tenantSource);
                    if (update == 0) {
                        return AjaxResult.error("修改失败，请检查修改信息，且该数据源未配置从数据源,无法启用！");
                    } else {
                        return AjaxResult.error("修改成功但启用失败，该数据源未配置从数据源,无法启用！");
                    }
                } else {
                    return AjaxResult.error("启用失败，该数据源未配置从数据源,无法启用！");
                }
            }
        } else if (StringUtils.equals(TenantConstants.DISABLE, tenantSource.getStatus())) {
            if (StringUtils.equals(TenantConstants.SOURCE_WRITE, tenantSource.getType()) || StringUtils.equals(TenantConstants.SOURCE_READ_WRITE, tenantSource.getType())) {
                if (!StringUtils.equals(oldSource.getStatus(), tenantSource.getStatus()) && tenantSourceService.checkStrategySourceBySourceId(check) > 0) {
                    if (key) {
                        tenantSource.setStatus(oldSource.getStatus());
                        update = tenantSourceService.updateTenantSource(tenantSource);
                        if (update == 0) {
                            return AjaxResult.error("修改失败，请检查修改信息，该数据源已被应用于策略,无法禁用！");
                        } else {
                            return AjaxResult.error("修改成功但禁用失败，该数据源已被应用于策略,无法禁用！");
                        }
                    } else {
                        return AjaxResult.error("禁用失败，该数据源已被应用于策略,无法禁用！");
                    }
                }
            } else if (StringUtils.equals(TenantConstants.SOURCE_READ, tenantSource.getType())) {
                if (!StringUtils.equals(oldSource.getStatus(), tenantSource.getStatus()) && tenantSourceService.checkSeparationSourceByReadId(check) > 0) {
                    if (key) {
                        tenantSource.setStatus(oldSource.getStatus());
                        update = tenantSourceService.updateTenantSource(tenantSource);
                        if (update == 0) {
                            return AjaxResult.error("修改失败，请检查修改信息，该数据源已被应用于主从库,无法禁用！");
                        } else {
                            return AjaxResult.error("修改成功但禁用失败，该数据源已被应用于主从库,无法禁用！");
                        }
                    } else {
                        return AjaxResult.error("禁用失败，该数据源已被应用于策略,无法禁用！");
                    }
                }
            }
        }
        return toAjax(tenantSourceService.updateTenantSource(tenantSource));
    }

    /**
     * 修改数据源排序
     */
    @PreAuthorize(hasPermi = "tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody TenantSource tenantSource) {
        if (tenantSource.getDatabaseType() != null && tenantSource.getDatabaseType().equals("1")) {
            return AjaxResult.error("禁止操作主数据源");
        }
        return toAjax(tenantSourceService.updateTenantSourceSort(tenantSource));
    }

    /**
     * 删除数据源
     */
    @PreAuthorize(hasPermi = "tenant:source:remove")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody TenantSource tenantSource) {
        return toAjax(tenantSourceService.deleteTenantSourceByIds(tenantSource));
    }
}