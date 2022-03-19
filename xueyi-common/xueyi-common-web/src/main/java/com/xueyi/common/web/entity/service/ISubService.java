package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.web.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层 主子类通用数据处理
 * 该接口不用于调用
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public interface ISubService<D extends BaseEntity, S extends BaseEntity> {

    /**
     * 根据外键查询子数据对象集合 | 子数据
     *
     * @param foreignKey 外键
     * @return 子数据对象集合
     */
    List<S> selectSubByForeignKey(Serializable foreignKey);

    /**
     * 根据Id修改其归属数据的状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    int updateSubStatus(Serializable id, String status);

    /**
     * 根据Id删除其归属数据
     *
     * @param id Id
     * @return 结果
     */
    int deleteSub(Serializable id);

    /**
     * 校验是否存在子数据
     *
     * @param id Id
     * @return 结果 | true/false 存在/不存在
     */
    boolean checkExistSub(Serializable id);

    /**
     * 校验是否存在启用（正常状态）的子数据
     *
     * @param id Id
     * @return 结果 | true/false 存在/不存在
     */
    boolean checkExistNormalSub(Serializable id);
}
