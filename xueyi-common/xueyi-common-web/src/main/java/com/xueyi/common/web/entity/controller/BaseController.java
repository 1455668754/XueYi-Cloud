package com.xueyi.common.web.entity.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.handle.BaseHandleController;
import com.xueyi.common.web.entity.service.IBaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作层 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @author xueyi
 */
public abstract class BaseController<D extends BaseEntity, DS extends IBaseService<D>> extends BaseHandleController<D, DS> {

    /**
     * 查询列表
     */
    public AjaxResult list(D d) {
        startPage();
        List<D> list = baseService.selectListScope(d);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    public void export(HttpServletResponse response, D d) {
        List<D> list = baseService.selectListScope(d);
        ExcelUtil<D> util = new ExcelUtil<D>(getClazz());
        util.exportExcel(response, list, StrUtil.format("{}数据", getNodeName()));
    }

    /**
     * 查询详细
     */
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return AjaxResult.success(baseService.selectById(id));
    }

    /**
     * 查询详细 | 附加数据
     */
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return AjaxResult.success(baseService.selectByIdExtra(id));
    }

    /**
     * 新增
     */
    public AjaxResult add(@Validated @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.ADD, d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 强制新增
     */
    public AjaxResult addForce(@Validated @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.ADD_FORCE, d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 修改
     */
    public AjaxResult edit(@Validated @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.EDIT, d);
        return toAjax(baseService.update(d));
    }

    /**
     * 强制修改
     */
    public AjaxResult editForce(@Validated @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.EDIT_FORCE, d);
        return toAjax(baseService.update(d));
    }

    /**
     * 修改状态
     */
    public AjaxResult editStatus(@RequestBody D d) {
        ESHandleValidated(BaseConstants.Operate.EDIT_STATUS, d);
        return toAjax(baseService.updateStatus(d.getId(), d.getStatus()));
    }

    /**
     * 强制修改状态
     */
    public AjaxResult editStatusForce(@RequestBody D d) {
        ESHandleValidated(BaseConstants.Operate.EDIT_STATUS_FORCE, d);
        return toAjax(baseService.updateStatus(d.getId(), d.getStatus()));
    }

    /**
     * 批量删除
     *
     * @see #RHandleEmptyValidated (List)  基类 空校验
     */
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        RHandleEmptyValidated(idList);
        RHandleValidated(BaseConstants.Operate.DELETE, idList);
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 强制批量删除
     *
     * @see #RHandleEmptyValidated (List)  基类 空校验
     */
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        List<Long> s = new ArrayList<>();
        RHandleEmptyValidated(idList);
        RHandleValidated(BaseConstants.Operate.DELETE_FORCE, idList);
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 获取选择框列表
     */
    public AjaxResult option() {
        D d = newBaseObject();
        d.setStatus(BaseConstants.Status.NORMAL.getCode());
        return list(d);
    }
}
