package com.xueyi.common.web.entity.controller.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.ISubTreeService;

import java.util.List;

/**
 * 操作层 操作方法 主子树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @param <S>  SubDto
 * @param <SS> SubService
 * @author xueyi
 */
public abstract class SubTreeHandleController<D extends SubTreeEntity<D, S>, DS extends ISubTreeService<D, S>, S extends BaseEntity, SS extends IBaseService<S>> extends TreeController<D, DS> {

    /** 定义子数据名称 */
    protected abstract String getSubName();

    /**
     * 主子树型 修改 归属数据状态逻辑校验
     *
     * @param d 待修改数据对象
     * @see com.xueyi.common.web.entity.controller.SubTreeController#edit(D)
     */
    protected void EHandleSubStatusValidated(D d) {
        if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                && baseService.checkExistNormalSub(d.getId()))
            throw new ServiceException(StrUtil.format("修改{}{}失败，该{}包含未停用的归属{}，禁止停用！", getNodeName(), d.getName(), getNodeName(), getSubName()));
    }

    /**
     * 主子树型 修改状态 归属数据状态逻辑校验
     *
     * @param d 待修改数据对象
     * @see com.xueyi.common.web.entity.controller.SubTreeController#editStatus(D)
     */
    protected void ESHandleSubStatusValidated(D d) {
        if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                && baseService.checkExistNormalSub(d.getId()))
            throw new ServiceException(StrUtil.format("停用失败，该{}包含未停用的归属{}！", getNodeName(), getSubName()));
    }

    /**
     * 主子树型 删除 子节点&&归属数据存在与否校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.SubTreeController#batchRemove(List)
     */
    protected void RHandleTreeSubValidated(List<Long> idList) {
        int size = idList.size();
        // remove node where nodeChildren && sub exist
        for (int i = size - 1; i >= 0; i--) {
            if (baseService.checkExistSub(idList.get(i)) || baseService.checkHasChild(idList.get(i)))
                idList.remove(i);
        }
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException(StrUtil.format("删除失败，所有待删除{}皆存在子{}或归属{}！", getNodeName(), getNodeName(), getSubName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            throw new ServiceException(StrUtil.format("成功删除所有无子{}或归属{}的{}！", getNodeName(), getSubName(), getNodeName()));
        }
    }
}
