package com.xueyi.tenant.controller;

import java.util.ArrayList;
import java.util.List;

import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.tenant.api.domain.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.service.ISourceService;
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
    private ISourceService tenantSourceService;

    /**
     * 查询数据源列表
     */
    @PreAuthorize(hasPermi = "tenant:source:list")
    @GetMapping("/list")
    public TableDataInfo list(Source source) {
        startPage();
        List<Source> list = tenantSourceService.mainSelectSourceList(source);
        return getDataTable(list);
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize(hasPermi = "tenant:source:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Source source) {
        return AjaxResult.success(tenantSourceService.mainSelectSourceBySourceId(source));
    }

    /**
     * 新增数据源
     */
    @PreAuthorize(hasPermi = "tenant:source:add")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Source source) {
        return toAjax(tenantSourceService.mainInsertSource(source));
    }

    /**
     * 修改数据源
     */
    @PreAuthorize(hasPermi = "tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Source source) {
        boolean key;
        int ds;//0不变 1刷新 2启动 3删除
        key = !StringUtils.equals(UserConstants.STATUS_UPDATE_OPERATION, source.getUpdateType());
        int update;
        Source check = new Source();
        check.setSourceId(source.getSourceId());
        Source oldSource = tenantSourceService.mainSelectSourceBySourceId(check);
        if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus()) && StringUtils.equals(TenantConstants.DISABLE, source.getStatus())) {
            ds = TenantConstants.SYNC_TYPE_DELETE;
        } else if (StringUtils.equals(TenantConstants.DISABLE, oldSource.getStatus()) && StringUtils.equals(TenantConstants.NORMAL, source.getStatus())) {
            ds = TenantConstants.SYNC_TYPE_ADD;
        } else if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus()) && StringUtils.equals(TenantConstants.NORMAL, source.getStatus()) && !(StringUtils.equals(oldSource.getDriverClassName(), source.getDriverClassName()) && StringUtils.equals(oldSource.getUrl(), source.getUrl()) && StringUtils.equals(oldSource.getUsername(), source.getUsername()) && StringUtils.equals(oldSource.getPassword(), source.getPassword()))) {
            ds = TenantConstants.SYNC_TYPE_REFRESH;
        } else {
            ds = TenantConstants.SYNC_TYPE_UNCHANGED;
        }
        source.setSyncType(ds);
        if (!key && ds != TenantConstants.SYNC_TYPE_UNCHANGED) {
            source.setSlave(oldSource.getSlave());
            source.setDriverClassName(oldSource.getDriverClassName());
            source.setUrl(oldSource.getUrl());
            source.setUsername(oldSource.getUsername());
            source.setPassword(oldSource.getPassword());
        }
        if (StringUtils.equals(TenantConstants.SOURCE_WRITE, source.getType()) && StringUtils.equals(TenantConstants.NORMAL, source.getStatus())) {
            if (!StringUtils.equals(oldSource.getStatus(), source.getStatus()) && tenantSourceService.mainCheckSeparationSourceByWriteId(check) == 0) {
                source.setStatus(oldSource.getStatus());
                if (key) {
                    update = tenantSourceService.mainUpdateSource(source, ds);
                    if (update == 0) {
                        return AjaxResult.error("修改失败，请检查修改信息，且该数据源未配置从数据源,无法启用！");
                    } else {
                        return AjaxResult.error("修改成功但启用失败，该数据源未配置从数据源,无法启用！");
                    }
                } else {
                    return AjaxResult.error("启用失败，该数据源未配置从数据源,无法启用！");
                }
            }
        } else if (StringUtils.equals(TenantConstants.DISABLE, source.getStatus())) {
            if (StringUtils.equals(TenantConstants.SOURCE_WRITE, source.getType()) || StringUtils.equals(TenantConstants.SOURCE_READ_WRITE, source.getType())) {
                if (!StringUtils.equals(oldSource.getStatus(), source.getStatus()) && tenantSourceService.mainCheckStrategySourceBySourceId(check) > 0) {
                    if (key) {
                        source.setStatus(oldSource.getStatus());
                        update = tenantSourceService.mainUpdateSource(source, ds);
                        if (update == 0) {
                            return AjaxResult.error("修改失败，请检查修改信息，该数据源已被应用于策略,无法禁用！");
                        } else {
                            return AjaxResult.error("修改成功但禁用失败，该数据源已被应用于策略,无法禁用！");
                        }
                    } else {
                        return AjaxResult.error("禁用失败，该数据源已被应用于策略,无法禁用！");
                    }
                }
            } else if (StringUtils.equals(TenantConstants.SOURCE_READ, source.getType())) {
                if (!StringUtils.equals(oldSource.getStatus(), source.getStatus()) && tenantSourceService.mainCheckSeparationSourceByReadId(check) > 0) {
                    if (key) {
                        source.setStatus(oldSource.getStatus());
                        update = tenantSourceService.mainUpdateSource(source, ds);
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
        return toAjax(tenantSourceService.mainUpdateSource(source, ds));
    }

    /**
     * 修改数据源排序
     */
    @PreAuthorize(hasPermi = "tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Source source) {
        return toAjax(tenantSourceService.mainUpdateSourceSort(source));
    }

    /**
     * 删除数据源 | 葫芦娃救爷爷，一个个判断是否允许删除
     */
    @PreAuthorize(hasPermi = "tenant:source:remove")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Source source) {
        boolean key = false;
        List<Long> Ids = (List<Long>) source.getParams().get("Ids");
        List<Source> DsIds = new ArrayList<>();
        for (int i = Ids.size() - 1; i >= 0; i--) {
            Source check = new Source();
            String sourceId = String.valueOf(Ids.get(i));
            check.setSourceId(Long.valueOf(sourceId));
            Source oldSource = tenantSourceService.mainSelectSourceBySourceId(check);
            if (StringUtils.equals(TenantConstants.MASTER_SOURCE, oldSource.getDatabaseType()) || ((StringUtils.equals(TenantConstants.SOURCE_WRITE, oldSource.getType()) || StringUtils.equals(TenantConstants.SOURCE_READ_WRITE, oldSource.getType())) && tenantSourceService.mainCheckStrategySourceBySourceId(check) > 0) || (StringUtils.equals(TenantConstants.SOURCE_READ, oldSource.getType()) && tenantSourceService.mainCheckSeparationSourceByReadId(check) > 0)) {
                Ids.remove(i);
                key = true;
            } else if (StringUtils.equals(TenantConstants.NORMAL, oldSource.getStatus())) {
                Source delDs = new Source();
                delDs.setSourceId(oldSource.getSourceId());
                delDs.setSlave(oldSource.getSlave());
                DsIds.add(delDs);
            }
        }
        if (DsIds.size() <= 0) {
            return AjaxResult.error("主数据源或已被应用于策略或主从的数据源无法被删除！");
        }
        int res = tenantSourceService.mainDeleteSourceByIds(DsIds);
        if(key){
            return AjaxResult.error("已被应用于策略或主从的数据源未成功删除！");
        }
        return toAjax(res);
    }
}