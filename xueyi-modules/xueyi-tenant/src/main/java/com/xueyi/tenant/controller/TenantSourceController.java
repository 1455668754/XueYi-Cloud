package com.xueyi.tenant.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.tenant.api.domain.source.TenantSource;
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
        int ds;//0不变 1刷新 2启动 3删除
        key = !StringUtils.equals(UserConstants.STATUS_UPDATE_OPERATION, tenantSource.getUpdateType());
        int update;
        TenantSource check = new TenantSource();
        check.setSourceId(tenantSource.getSourceId());
        TenantSource oldSource = tenantSourceService.selectTenantSourceById(check);
        if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus()) && StringUtils.equals(TenantConstants.DISABLE, tenantSource.getStatus())) {
            ds = TenantConstants.SYNC_TYPE_DELETE;
        } else if (StringUtils.equals(TenantConstants.DISABLE, oldSource.getStatus()) && StringUtils.equals(TenantConstants.NORMAL, tenantSource.getStatus())) {
            ds = TenantConstants.SYNC_TYPE_ADD;
        } else if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus()) && StringUtils.equals(TenantConstants.NORMAL, tenantSource.getStatus()) && !(StringUtils.equals(oldSource.getDriverClassName(), tenantSource.getDriverClassName()) && StringUtils.equals(oldSource.getUrl(), tenantSource.getUrl()) && StringUtils.equals(oldSource.getUsername(), tenantSource.getUsername()) && StringUtils.equals(oldSource.getPassword(), tenantSource.getPassword()))) {
            ds = TenantConstants.SYNC_TYPE_REFRESH;
        } else {
            ds = TenantConstants.SYNC_TYPE_UNCHANGED;
        }
        tenantSource.setSyncType(ds);
        if (!key && ds != TenantConstants.SYNC_TYPE_UNCHANGED) {
            tenantSource.setSlave(oldSource.getSlave());
            tenantSource.setDriverClassName(oldSource.getDriverClassName());
            tenantSource.setUrl(oldSource.getUrl());
            tenantSource.setUsername(oldSource.getUsername());
            tenantSource.setPassword(oldSource.getPassword());
        }
        if (StringUtils.equals(TenantConstants.SOURCE_WRITE, tenantSource.getType()) && StringUtils.equals(TenantConstants.NORMAL, tenantSource.getStatus())) {
            if (!StringUtils.equals(oldSource.getStatus(), tenantSource.getStatus()) && tenantSourceService.checkSeparationSourceByWriteId(check) == 0) {
                tenantSource.setStatus(oldSource.getStatus());
                if (key) {
                    update = tenantSourceService.updateTenantSource(tenantSource, ds);
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
                        update = tenantSourceService.updateTenantSource(tenantSource, ds);
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
                        update = tenantSourceService.updateTenantSource(tenantSource, ds);
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
        return toAjax(tenantSourceService.updateTenantSource(tenantSource, ds));
    }

    /**
     * 修改数据源排序
     */
    @PreAuthorize(hasPermi = "tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody TenantSource tenantSource) {
        return toAjax(tenantSourceService.updateTenantSourceSort(tenantSource));
    }

    /**
     * 删除数据源 | 葫芦娃救爷爷，一个个判断是否允许删除
     */
    @PreAuthorize(hasPermi = "tenant:source:remove")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody TenantSource tenantSource) {
        boolean key = false;
        List<Long> Ids = (List<Long>) tenantSource.getParams().get("Ids");
        List<TenantSource> DsIds = new ArrayList<>();
        for (int i = Ids.size() - 1; i >= 0; i--) {
            TenantSource check = new TenantSource();
            String sourceId = String.valueOf(Ids.get(i));
            check.setSourceId(Long.valueOf(sourceId));
            TenantSource oldSource = tenantSourceService.selectTenantSourceById(check);
            if (StringUtils.equals(TenantConstants.MASTER_SOURCE, oldSource.getDatabaseType()) || ((StringUtils.equals(TenantConstants.SOURCE_WRITE, oldSource.getType()) || StringUtils.equals(TenantConstants.SOURCE_READ_WRITE, oldSource.getType())) && tenantSourceService.checkStrategySourceBySourceId(check) > 0) || (StringUtils.equals(TenantConstants.SOURCE_READ, oldSource.getType()) && tenantSourceService.checkSeparationSourceByReadId(check) > 0)) {
                Ids.remove(i);
                key = true;
            } else if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus())) {
                TenantSource delDs = new TenantSource();
                delDs.setSourceId(oldSource.getSourceId());
                delDs.setSlave(oldSource.getSlave());
                DsIds.add(delDs);
            }
        }
        if (Ids.size() <= 0) {
            return AjaxResult.error("删除失败，数据源已被应用于策略或主从！");
        }
        int res = tenantSourceService.deleteTenantSourceByIds(tenantSource, DsIds);
        if(key){
            return AjaxResult.error("已被应用于策略或主从的数据源未成功删除！");
        }
        return toAjax(res);
    }
}