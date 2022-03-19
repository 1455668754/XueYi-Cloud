package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 服务层 基类通用数据处理
 *
 * @param <D> Dto
 * @author xueyi
 */
public interface IBaseService<D extends BaseEntity> {

    /**
     * 查询数据对象列表
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    List<D> selectList(D d);

    /**
     * 查询数据对象列表 | 附加数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    List<D> selectListExtra(D d);

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    List<D> selectListScope(D d);

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    public List<D> selectListByIds(Collection<? extends Serializable> idList);

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    D selectById(Serializable id);

    /**
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    D selectByIdExtra(Serializable id);

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    int insert(D d);

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    int insertBatch(Collection<D> entityList);

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    int update(D d);

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    int updateBatch(Collection<D> entityList);

    /**
     * 修改数据对象状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    int updateStatus(Serializable id, String status);

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    int deleteById(Serializable id);

    /**
     * 根据Id集合删除数据对象
     *
     * @param idList Id集合
     * @return 结果
     */
    int deleteByIds(Collection<? extends Serializable> idList);

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkNameUnique(Serializable id, String name);

    /**
     * 根据Id查询数据对象状态
     *
     * @param id Id
     * @return 状态
     */
    BaseConstants.Status checkStatus(Serializable id);
}
