package com.xueyi.common.web.entity.mapper;

import com.xueyi.common.core.web.entity.base.BasisEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 数据层 基类通用数据处理
 *
 * @param <D> Dto
 * @author xueyi
 */
public interface BaseMapper<D extends BasisEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<D> {

    /**
     * 自定义批量插入
     */
    int insertBatch(@Param("collection") Collection<D> list);

    /**
     * 自定义批量更新，条件为主键
     */
    int updateBatch(@Param("collection") Collection<D> list);
}
