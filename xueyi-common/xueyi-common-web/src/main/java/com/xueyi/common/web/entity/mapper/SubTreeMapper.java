package com.xueyi.common.web.entity.mapper;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;

/**
 * 数据层 主子树型通用数据处理
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public interface SubTreeMapper<D extends SubTreeEntity<D, S>, S extends BaseEntity> extends TreeMapper<D>{
}
