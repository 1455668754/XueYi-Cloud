package com.xueyi.system.dict.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.SubBaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 字典类型管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends SubBaseController<SysDictTypeDto, ISysDictTypeService, SysDictDataDto, ISysDictDataService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "字典类型";
    }

    /** 定义子数据名称 */
    @Override
    protected String getSubName() {
        return "字典数据";
    }

    /**
     * 查询字典类型列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_DICT_LIST)
    public AjaxResult list(SysDictTypeDto dictType) {
        return super.list(dictType);
    }

    /**
     * 查询字典类型详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_DICT_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 字典类型导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_DICT_EXPORT)
    @Log(title = "字典类型管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysDictTypeDto dictType) {
        super.export(response, dictType);
    }

    /**
     * 字典类型新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_DICT_ADD)
    @Log(title = "字典类型管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDictTypeDto dictType) {
        return super.add(dictType);
    }

    /**
     * 字典类型修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_DICT_EDIT)
    @Log(title = "字典类型管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDictTypeDto dictType) {
        return super.edit(dictType);
    }

    /**
     * 字典类型修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_DICT_EDIT, Auth.SYS_DICT_ES}, logical = Logical.OR)
    @Log(title = "字典类型管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDictTypeDto dictType) {
        return super.editStatus(dictType);
    }

    /**
     * 字典类型批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_DICT_DELETE)
    @Log(title = "字典类型管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 字典类型强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @RequiresPermissions(Auth.SYS_DICT_DELETE)
    @Log(title = "字典类型管理", businessType = BusinessType.DELETE_FORCE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }

    /**
     * 刷新字典缓存
     */
    @RequiresPermissions(Auth.SYS_DICT_EDIT)
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @GetMapping("/refresh")
    public AjaxResult refreshCache() {
        baseService.resetDictCache();
        return AjaxResult.success();
    }

    /**
     * 获取字典类型选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandleValidated(BaseConstants.Operate operate, SysDictTypeDto dictType) {
        if (baseService.checkDictCodeUnique(dictType.getId(), dictType.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，字典编码已存在", operate.getInfo(), getNodeName(), dictType.getName()));
    }
}
