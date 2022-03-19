package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;

/**
 * 服务层 主子树型通用数据处理
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public interface ISubTreeService<D extends SubTreeEntity<D, S>, S extends BaseEntity> extends ITreeService<D>, ISubService<D, S> {
}
