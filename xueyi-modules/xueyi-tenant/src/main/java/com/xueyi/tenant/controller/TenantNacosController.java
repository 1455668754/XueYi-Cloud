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
import com.xueyi.tenant.domain.TenantNacos;
import com.xueyi.tenant.service.ITenantNacosService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * Nacos配置 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/nacos")
public class TenantNacosController extends BaseController
{
    @Autowired
    private ITenantNacosService tenantNacosService;

    /**
     * 查询Nacos配置列表
     */
    @PreAuthorize(hasPermi = "tenant:nacos:list")
    @GetMapping("/list")
    public TableDataInfo list(TenantNacos tenantNacos)
    {
        startPage();
        List<TenantNacos> list = tenantNacosService.selectTenantNacosList(tenantNacos);
        return getDataTable(list);
    }


    /**
     * 导出Nacos配置列表
     */
    @PreAuthorize(hasPermi = "tenant:nacos:export")
    @Log(title = "Nacos配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TenantNacos tenantNacos) throws IOException
    {
        List<TenantNacos> list = tenantNacosService.selectTenantNacosList(tenantNacos);
        ExcelUtil<TenantNacos> util = new ExcelUtil<TenantNacos>(TenantNacos.class);
        util.exportExcel(response, list, "Nacos配置数据");
    }

    /**
     * 获取Nacos配置详细信息
     */
    @PreAuthorize(hasPermi = "tenant:nacos:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(TenantNacos tenantNacos)
    {
        return AjaxResult.success(tenantNacosService.selectTenantNacosById(tenantNacos));
    }

    /**
     * 新增Nacos配置
     */
    @PreAuthorize(hasPermi = "tenant:nacos:add")
    @Log(title = "Nacos配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TenantNacos tenantNacos)
    {
        return toAjax(tenantNacosService.insertTenantNacos(tenantNacos));
    }

    /**
     * 修改Nacos配置
     */
    @PreAuthorize(hasPermi = "tenant:nacos:edit")
    @Log(title = "Nacos配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TenantNacos tenantNacos)
    {
        return toAjax(tenantNacosService.updateTenantNacos(tenantNacos));
    }

    /**
     * 修改Nacos配置排序
     */
    @PreAuthorize(hasPermi = "tenant:nacos:edit")
    @Log(title = "Nacos配置", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody TenantNacos tenantNacos)
    {
        return toAjax(tenantNacosService.updateTenantNacosSort(tenantNacos));
    }

    /**
     * 删除Nacos配置
     */
    @PreAuthorize(hasPermi = "tenant:nacos:remove")
    @Log(title = "Nacos配置", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody TenantNacos tenantNacos)
    {
        return toAjax(tenantNacosService.deleteTenantNacosByIds(tenantNacos));
    }
}