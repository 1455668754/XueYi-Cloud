package com.xueyi.common.web.entity.service.impl.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.manager.TreeManager;
import com.xueyi.common.web.entity.mapper.TreeMapper;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;

import java.io.Serializable;

/**
 * 服务层 操作方法 树型实现通用数据处理
 *
 * @param <D>  Dto
 * @param <DG> DtoManager
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class TreeHandleServiceImpl<D extends TreeEntity<D>, DG extends TreeManager<D, DM>, DM extends TreeMapper<D>> extends BaseServiceImpl<D, DG, DM> {

    /**
     * 新增/修改 树型 检查父级状态
     * 是否启用，非启用则启用
     *
     * @param parentId 父级Id
     * @param status   状态
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#insert(TreeEntity)
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     */
    protected void AUHandleParentStatusCheck(Serializable parentId, String status) {
        if (ObjectUtil.equals(BaseConstants.Status.NORMAL.getCode(), status)) {
            BaseConstants.Status parentStatus = checkStatus(parentId);
            if (BaseConstants.Status.DISABLE == parentStatus)
                baseManager.updateStatus(parentId, BaseConstants.Status.NORMAL.getCode());
        }
    }

    /**
     * 修改状态 树型 检查父级状态
     * 是否启用，非启用则启用
     *
     * @param Id     Id
     * @param status 状态
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#updateStatus(Serializable, String)
     */
    protected void USHandelParentStatusCheck(Serializable Id, String status) {
        D nowD = baseManager.selectById(Id);
        if (ObjectUtil.equals(BaseConstants.Status.NORMAL.getCode(), status)
                && BaseConstants.Status.DISABLE == checkStatus(nowD.getParentId()))
            baseManager.updateStatus(nowD.getParentId(), BaseConstants.Status.NORMAL.getCode());
    }

    /**
     * 新增 树型 设置祖籍
     *
     * @param d 数据对象 | parentId 父Id
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#insert(TreeEntity)
     */
    protected void AHandleAncestorsSet(D d) {
        if (ObjectUtil.equals(BaseConstants.TOP_ID, d.getParentId())) {
            d.setAncestors(String.valueOf(BaseConstants.TOP_ID));
        } else {
            D parent = baseManager.selectById(d.getParentId());
            d.setAncestors(parent.getAncestors() + StrUtil.COMMA + d.getParentId());
        }
    }

    /**
     * 修改 树型 检验祖籍是否变更
     * 是否变更，变更则同步变更子节点祖籍
     *
     * @param d 数据对象 | id id | parentId 父Id
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     */
    protected void UHandleAncestorsCheck(D d) {
        D original = baseManager.selectById(d.getId());
        if (!ObjectUtil.equals(d.getParentId(), original.getParentId())) {
            String oldAncestors = original.getAncestors();
            if (ObjectUtil.equals(BaseConstants.TOP_ID, d.getParentId()))
                d.setAncestors(String.valueOf(BaseConstants.TOP_ID));
            else {
                D parent = baseManager.selectById(d.getParentId());
                d.setAncestors(parent.getAncestors() + StrUtil.COMMA + d.getParentId());
            }
            baseManager.updateChildrenAncestors(d.getId(), d.getAncestors(), oldAncestors);
        }
    }

    /**
     * 修改/修改状态 树型 检查子节点状态
     * 是否变更，变更则同步禁用子节点
     *
     * @param id     id
     * @param status 状态
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#update(TreeEntity)
     * @see com.xueyi.common.web.entity.service.impl.TreeServiceImpl#updateStatus(Serializable, String)
     */
    protected void UUSChildrenStatusCheck(Serializable id, String status) {
        D original = baseManager.selectById(id);
        if (ObjectUtil.notEqual(original.getStatus(), status)
                && ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), status)
                && ObjectUtil.isNotNull(baseManager.checkHasNormalChild(id))) {
            baseManager.updateChildrenStatus(id, BaseConstants.Status.DISABLE.getCode());
        }
    }
}
