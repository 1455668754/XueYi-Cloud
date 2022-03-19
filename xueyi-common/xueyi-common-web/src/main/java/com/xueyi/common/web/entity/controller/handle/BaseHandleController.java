package com.xueyi.common.web.entity.controller.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.common.web.entity.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 操作层 操作方法 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @author xueyi
 */
public abstract class BaseHandleController<D extends BaseEntity, DS extends IBaseService<D>> extends BasisController {

    @Autowired
    protected DS baseService;

    /** 定义节点名称 */
    protected abstract String getNodeName();

    /**
     * 获取D.class
     *
     * @return class
     */
    protected Class<D> getClazz() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return (Class<D>) pType.getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * new D
     *
     * @return D
     */
    protected D newBaseObject() {
        Class<D> clazz = getClazz();
        try {
            if(clazz != null)
                return clazz.newInstance();
        }catch (Exception ignored){}
        return null;
    }

    /**
     * 前置校验 （强制）增加/修改
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void AEHandleValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）修改状态
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param d       数据对象
     */
    protected void ESHandleValidated(BaseConstants.Operate operate, D d) {
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    protected void RHandleValidated(BaseConstants.Operate operate, List<Long> idList) {
    }

    /**
     * 删除 空校验
     *
     * @param idList 待删除Id集合
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemove(List)
     * @see com.xueyi.common.web.entity.controller.BaseController#batchRemoveForce(List)
     */
    protected void RHandleEmptyValidated(List<Long> idList) {
        if (CollUtil.isEmpty(idList))
            throw new ServiceException(StrUtil.format("无待删除{}！", getNodeName()));
    }
}
