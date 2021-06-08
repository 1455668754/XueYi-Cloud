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
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.service.ITenantService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * 租户信息 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController
{
    @Autowired
    private ITenantService tenantService;

    /**
     * 查询租户信息列表
     */
    @PreAuthorize(hasPermi = "tenant:tenant:list")
    @GetMapping("/list")
    public TableDataInfo list(Tenant tenant)
    {
        startPage();
        List<Tenant> list = tenantService.selectTenantList(tenant);
        return getDataTable(list);
    }


    /**
     * 导出租户信息列表
     */
    @PreAuthorize(hasPermi = "tenant:tenant:export")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tenant tenant) throws IOException
    {
        List<Tenant> list = tenantService.selectTenantList(tenant);
        ExcelUtil<Tenant> util = new ExcelUtil<Tenant>(Tenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Tenant tenant)
    {
        return AjaxResult.success(tenantService.selectTenantById(tenant));
    }

    /**
     * 新增租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:add")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tenant tenant)
    {
        return toAjax(tenantService.insertTenant(tenant));
    }

    /**
     * 修改租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tenant tenant)
    {
        if(tenant.getIsChange().equals("Y")){
            return AjaxResult.error("禁止操作系统租户");
        }
        return toAjax(tenantService.updateTenant(tenant));
    }

    /**
     * 修改租户信息排序
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Tenant tenant)
    {
        return toAjax(tenantService.updateTenantSort(tenant));
    }

    /**
     * 删除租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:remove")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Tenant tenant)
    {
        return toAjax(tenantService.deleteTenantByIds(tenant));
    }
}