package com.xueyi.tenant.controller;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.datasource.utils.DSUtils;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.tenant.api.domain.source.Source;
import com.xueyi.tenant.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class SourceController extends BaseController {

    @Autowired
    private ISourceService sourceService;

    /**
     * 查询数据源列表
     */
    @RequiresPermissions("tenant:source:list")
    @GetMapping("/list")
    public AjaxResult list(Source source) {
        startPage();
        List<Source> list = sourceService.mainSelectSourceList(source);
        return getDataTable(list);
    }

    /**
     * 获取数据源详细信息
     */
    @RequiresPermissions("tenant:source:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Source source) {
        return AjaxResult.success(sourceService.mainSelectSourceBySourceId(source));
    }

    /**
     * 新增数据源
     */
    @RequiresPermissions("tenant:source:add")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Source source) {
        DSUtils.testDs(source);
        return toAjax(sourceService.mainInsertSource(source));
    }

    /**
     * 修改数据源
     */
    @RequiresPermissions("tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Source source) {
        DSUtils.testDs(source);
        return toAjax(sourceService.mainUpdateSource(source));
    }

    /**
     * 修改数据源状态
     */
    @RequiresPermissions("tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult editStatus(@RequestBody Source source) {
        Source check = new Source(source.getSourceId());
        Source oldSource = sourceService.mainSelectSourceBySourceId(check);
        DSUtils.testDs(oldSource);
        if(StringUtils.equals(TenantConstants.NORMAL, source.getStatus())){
            if(StringUtils.equals(oldSource.getType(), TenantConstants.SOURCE_WRITE) && sourceService.mainCheckSeparationSourceByWriteId(check)){
                return AjaxResult.error("该数据源未配置读数据源,请先进行读写配置再启用！");
            }
        }else if(StringUtils.equals(TenantConstants.DISABLE, source.getStatus())){
            if(StringUtils.equals(oldSource.getType(), TenantConstants.SOURCE_READ) && sourceService.mainCheckSeparationSourceByReadId(check)){
                return AjaxResult.error("该数据源已被应用于读写配置,请先从对应读写配置中取消关联后再禁用！");
            }else if((StringUtils.equals(oldSource.getType(), TenantConstants.SOURCE_READ_WRITE) || StringUtils.equals(oldSource.getType(), TenantConstants.SOURCE_WRITE)) && sourceService.mainCheckStrategySourceBySourceId(check)){
                return AjaxResult.error("该数据源已被应用于数据源策略,请先从对应策略中取消关联后再禁用！");
            }
        }
        if (StringUtils.equals(source.getStatus(), oldSource.getStatus())) {
            source.setSyncType(TenantConstants.SYNC_TYPE_UNCHANGED);
            return AjaxResult.error("无状态调整！");
        } else {
            if (StringUtils.equals(source.getStatus(), TenantConstants.DISABLE)) {
                source.setSyncType(TenantConstants.SYNC_TYPE_DELETE);
            } else if (StringUtils.equals(source.getStatus(), TenantConstants.NORMAL)) {
                source.setSyncType(TenantConstants.SYNC_TYPE_ADD);
                source.setDriverClassName(oldSource.getDriverClassName());
                source.setUrl(oldSource.getUrlPrepend().concat(oldSource.getUrlAppend()));
                source.setUsername(oldSource.getUsername());
                source.setPassword(oldSource.getPassword());
            }
            source.setSlave(oldSource.getSlave());
        }
        return toAjax(sourceService.mainUpdateSourceStatus(source));
    }

    /**
     * 修改数据源排序
     */
    @RequiresPermissions("tenant:source:edit")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Source source) {
        return toAjax(sourceService.mainUpdateSourceSort(source));
    }

    /**
     * 删除数据源 | 单个删除
     */
    @RequiresPermissions("tenant:source:remove")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Source source) {
        Source check = sourceService.mainSelectSourceBySourceId(source);
        if (StringUtils.equals(check.getStatus(), TenantConstants.NORMAL)) {
            return AjaxResult.error("请先停用数据源后再删除！");
        } else if (StringUtils.equals(check.getIsChange(), Constants.SYSTEM_DEFAULT_TRUE)) {
            return AjaxResult.error("系统默认数据源无法被删除！");
        }
        return toAjax(sourceService.mainDeleteSourceById(check));
    }
}