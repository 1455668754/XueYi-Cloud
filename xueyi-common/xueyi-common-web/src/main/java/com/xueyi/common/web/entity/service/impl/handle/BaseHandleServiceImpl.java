package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务层 操作方法 基类实现通用数据处理
 *
 * @param <D>  Dto
 * @param <DG> DtoManager
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class BaseHandleServiceImpl<D extends BaseEntity, DG extends BaseManager<D, DM>, DM extends BaseMapper<D>> {

    @Autowired
    protected DG baseManager;
}
