package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.web.entity.controller.handle.TreeHandleController;
import com.xueyi.common.web.entity.service.ITreeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 操作层 树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @author xueyi
 */
public abstract class TreeController<D extends TreeEntity<D>, DS extends ITreeService<D>> extends TreeHandleController<D, DS> {

    /**
     * 查询树列表
     */
    @Override
    public AjaxResult list(D d) {
        List<D> list = baseService.selectListScope(d);
        return AjaxResult.success(TreeUtils.buildTree(list));
    }

    /**
     * 查询树列表（排除节点）
     */
    public AjaxResult listExNodes(D d) {
        Serializable id = d.getId();
        d.setId(null);
        List<D> list = baseService.selectListScope(d);
        SHandleExNodes(list, id);
        return AjaxResult.success(TreeUtils.buildTree(list));
    }

    /**
     * 树型 新增
     * 考虑父节点状态
     *
     * @see #AHandleTreeStatusValidated(D) 树型 父节点逻辑校验
     */
    @Override
    public AjaxResult add(@Validated({V_A.class}) @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.ADD, d);
        AHandleTreeStatusValidated(d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 树型 修改
     * 考虑子节点状态
     *
     * @see #EHandleTreeLoopValidated(D) 树型 父节点不能为自己或自己的子节点
     * @see #EHandleTreeStatusValidated(D) 树型 父子节点逻辑校验
     */
    @Override
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.EDIT, d);
        EHandleTreeLoopValidated(d);
        EHandleTreeStatusValidated(d);
        return toAjax(baseService.update(d));
    }

    /**
     * 树型 强制修改
     *
     * @see #EHandleTreeLoopValidated(D) 树型 父节点不能为自己或自己的子节点
     */
    @Override
    public AjaxResult editForce(@Validated({V_E.class}) @RequestBody D d) {
        AEHandleValidated(BaseConstants.Operate.EDIT_FORCE, d);
        EHandleTreeLoopValidated(d);
        return toAjax(baseService.update(d));
    }

    /**
     * 树型 修改状态
     * 考虑子节点状态
     *
     * @see #ESHandleTreeStatusValidated(D) 树型 父子节点逻辑校验
     */
    @Override
    public AjaxResult editStatus(@RequestBody D d) {
        ESHandleTreeStatusValidated(d);
        return toAjax(baseService.updateStatus(d.getId(), d.getStatus()));
    }

    /**
     * 树型 批量删除
     * 考虑子节点存在与否
     *
     * @see #RHandleEmptyValidated(List)  基类 空校验
     * @see #RHandleTreeChildValidated(List)  树型 子节点存在与否校验
     */
    @Override
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        RHandleEmptyValidated(idList);
        RHandleValidated(BaseConstants.Operate.DELETE, idList);
        RHandleEmptyValidated(idList);
        RHandleTreeChildValidated(idList);
        return toAjax(baseService.deleteByIds(idList));
    }
}
