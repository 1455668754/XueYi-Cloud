package com.xueyi.system.dict.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.dict.service.ISysConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 参数配置管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController<SysConfigDto, ISysConfigService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "参数";
    }

    /**
     * 查询参数值
     */
    @GetMapping(value = "/innerCode/{configCode}")
    public R<String> getCode(@PathVariable String configCode) {
        return R.ok(baseService.selectConfigByCode(configCode));
    }

    /**
     * 查询参数值
     */
    @GetMapping(value = "/code/{configCode}")
    public AjaxResult getConfigCode(@PathVariable String configCode) {
        return AjaxResult.success(baseService.selectConfigByCode(configCode));
    }

    /**
     * 查询参数列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_CONFIG_LIST)
    public AjaxResult list(SysConfigDto config) {
        return super.list(config);
    }

    /**
     * 查询参数详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_CONFIG_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 参数导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_CONFIG_EXPORT)
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysConfigDto config) {
        super.export(response, config);
    }

    /**
     * 参数新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_CONFIG_ADD)
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysConfigDto config) {
        return super.add(config);
    }

    /**
     * 参数修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_CONFIG_EDIT)
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysConfigDto config) {
        return super.edit(config);
    }

    /**
     * 参数修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_CONFIG_EDIT, Auth.SYS_CONFIG_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "参数管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysConfigDto config) {
        return super.editStatus(config);
    }

    /**
     * 参数批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_CONFIG_DELETE)
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 参数强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @RequiresPermissions(Auth.SYS_CONFIG_FORCE_DELETE)
    @Log(title = "参数管理", businessType = BusinessType.DELETE_FORCE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }


    /**
     * 刷新参数缓存
     */
    @RequiresPermissions(Auth.SYS_CONFIG_EDIT)
    @Log(title = "参数管理", businessType = BusinessType.REFRESH)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        baseService.resetConfigCache();
        return AjaxResult.success();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandleValidated(BaseConstants.Operate operate, SysConfigDto config) {
        if (baseService.checkConfigCodeUnique(config.getId(), config.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，参数编码已存在", operate.getInfo(), getNodeName(), config.getName()));
    }

    /**
     * 前置校验 （强制）删除
     *
     * @param idList Id集合
     */
    @Override
    protected void RHandleValidated(BaseConstants.Operate operate, List<Long> idList) {
        if (operate.isDelete()) {
            int size = idList.size();
            // remove oneself or admin
            for (int i = idList.size() - 1; i >= 0; i--) {
                if (baseService.checkIsBuiltIn(idList.get(i)))
                    idList.remove(i);
            }
            if (CollUtil.isEmpty(idList)) {
                throw new ServiceException(StrUtil.format("{}失败，不能删除内置参数！", operate.getInfo()));
            } else if (idList.size() != size) {
                baseService.deleteByIds(idList);
                throw new ServiceException(StrUtil.format("成功{}除内置参数外的所有参数！", operate.getInfo()));
            }
        }
    }
}
