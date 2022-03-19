package com.xueyi.common.web.entity.service.impl.handle;

import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;
import com.xueyi.common.web.entity.manager.SubTreeManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;

/**
 * 服务层 操作方法 主子树型实现通用数据处理
 *
 * @param <D>  Dto
 * @param <DG> DtoManager
 * @param <DM> DtoMapper
 * @param <S>  SubDto
 * @param <SS> SubService
 * @param <SM> SubMapper
 * @author xueyi
 */
public abstract class SubTreeHandleServiceImpl<D extends SubTreeEntity<D, S>, DG extends SubTreeManager<D, DM, S, SM>, DM extends SubTreeMapper<D, S>, S extends BaseEntity, SS extends IBaseService<S>, SM extends BaseMapper<S>> extends TreeServiceImpl<D, DG, DM> {

    @Autowired
    protected SS subService;

    /**
     * 修改/修改状态 主子树型 检查归属数据状态
     * 是否变更，变更则同步禁用归属数据
     *
     * @param id     Id
     * @param status 状态
     * @see com.xueyi.common.web.entity.service.impl.SubTreeServiceImpl#update(SubTreeEntity)
     * @see com.xueyi.common.web.entity.service.impl.SubTreeServiceImpl#updateStatus(Serializable, String)
     */
    protected void UUSHandleSubStatusCheck(Serializable id, String status) {
        D original = baseManager.selectById(id);
        if (!ObjectUtil.equals(original.getStatus(), status)
                && ObjectUtil.equals(BaseConstants.Status.DISABLE.getCode(), status)
                && ObjectUtil.isNotNull(baseManager.checkExistNormalSub(id)))
            baseManager.updateSubStatus(id, BaseConstants.Status.DISABLE.getCode());
    }

    /**
     * 设置子数据的外键值
     *
     * @param subList 子数据集合
     * @param d       数据对象
     */
    protected void setForeignKey(Collection<S> subList, D d) {
        setForeignKey(subList, null, d, null);
    }

    /**
     * 设置子数据的外键值
     *
     * @param sub 子数据
     * @param d   数据对象
     */
    protected void setForeignKey(S sub, D d) {
        setForeignKey(null, sub, d, null);
    }

    /**
     * 设置子数据的外键值
     *
     * @param subList    子数据集合
     * @param foreignKey 子表外键值
     */
    protected void setForeignKey(Collection<S> subList, Serializable foreignKey) {
        setForeignKey(subList, null, null, foreignKey);
    }

    /**
     * 设置子数据的外键值
     *
     * @param sub        子数据
     * @param foreignKey 子表外键值
     */
    protected void setForeignKey(S sub, Serializable foreignKey) {
        setForeignKey(null, sub, null, foreignKey);
    }

    /**
     * 设置子数据的外键值
     *
     * @param subList    子数据集合
     * @param sub        子数据
     * @param d          数据对象
     * @param foreignKey 子表外键值
     */
    protected abstract void setForeignKey(Collection<S> subList, S sub, D d, Serializable foreignKey);
}
