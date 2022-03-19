package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;

/**
 * 服务层 主子基类通用数据处理
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public interface ISubBaseService<D extends SubBaseEntity<S>, S extends BaseEntity> extends IBaseService<D>,ISubService<D,S> {
}
