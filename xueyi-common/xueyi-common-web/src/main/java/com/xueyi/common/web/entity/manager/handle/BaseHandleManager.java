package com.xueyi.common.web.entity.manager.handle;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 数据封装层 操作方法 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @author xueyi
 */

public class BaseHandleManager<D extends BaseEntity, DM extends BaseMapper<D>> {

    @Autowired
    protected DM baseMapper;

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
}
