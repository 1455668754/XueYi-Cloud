package com.xueyi.common.web.entity.controller.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.ISubBaseService;

import java.util.List;

/**
 * 操作层 操作方法 主子基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @param <S>  SubDto
 * @param <SS> SubService
 * @author xueyi
 */
public abstract class SubBaseHandleController<D extends SubBaseEntity<S>, DS extends ISubBaseService<D, S>, S extends BaseEntity, SS extends IBaseService<S>> extends BaseController<D, DS> {

    /** 定义子数据名称 */
    protected abstract String getSubName();

    /**
     * 主子型 修改 归属数据状态逻辑校验
     *
     * @param d 待修改数据对象
     * @see com.xueyi.common.web.entity.controller.SubBaseController#edit(D)
     */
    protected void EHandleSubStatusValidated(D d) {
        if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                && baseService.checkExistNormalSub(d.getId()))
            throw new ServiceException(StrUtil.format("修改{}{}失败，该{}包含未停用的归属{}，禁止停用！", getNodeName(), d.getName(), getNodeName(), getSubName()));
    }

    /**
     * 主子型 修改状态 归属数据状态逻辑校验
     *
     * @param d 待修改数据对象
     * @see com.xueyi.common.web.entity.controller.SubBaseController#editStatus(D)
     */
    protected void ESHandleSubStatusValidated(D d) {
        if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), d.getStatus())
                && baseService.checkExistNormalSub(d.getId()))
            throw new ServiceException(StrUtil.format("停用失败，该{}包含未停用的归属{}！", getNodeName(), getSubName()));
    }

    /**
     * 主子型 删除 归属数据存在与否校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.SubBaseController#batchRemove(List)
     */
    protected void RHandleSubValidated(List<Long> idList) {
        int size = idList.size();
        // remove node where sub exist
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (baseService.checkExistSub(idList.get(i)))
                idList.remove(i);
        }
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException(StrUtil.format("删除失败，所有待删除{}皆存在归属{}！", getNodeName(), getSubName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            throw new ServiceException(StrUtil.format("成功删除所有无归属{}的{}！", getSubName(), getNodeName()));
        }
    }
}
