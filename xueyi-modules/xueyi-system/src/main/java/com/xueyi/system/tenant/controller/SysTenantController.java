package com.xueyi.system.tenant.controller;

import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.tenant.domain.SysTenant;
import com.xueyi.system.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class SysTenantController extends BaseController
{
    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 查询租户管理列表
     */
    @PreAuthorize(hasPermi = "system:tenant:list")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant sysTenant)
    {
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTable(list);
    }

    /**
     * 导出租户管理列表
     */
    @PreAuthorize(hasPermi = "system:tenant:export")
    @Log(title = "租户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant) throws IOException
    {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "租户管理数据");
    }

    /**
     * 获取租户管理详细信息
     */
    @PreAuthorize(hasPermi = "system:tenant:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(SysTenant sysTenant)
    {
        return AjaxResult.success(sysTenantService.selectSysTenantById(sysTenant));
    }

    /**
     * 新增租户管理
     */
    @PreAuthorize(hasPermi = "system:tenant:add")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.insertSysTenant(sysTenant));
    }

    /**
     * 修改租户管理
     */
    @PreAuthorize(hasPermi = "system:tenant:edit")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.updateSysTenant(sysTenant));
    }

    /**
     * 删除租户管理
     */
    @PreAuthorize(hasPermi = "system:tenant:remove")
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.deleteSysTenantById(sysTenant));
    }
}