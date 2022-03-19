package com.xueyi.common.web.entity.manager;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.manager.handle.BaseHandleManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据封装层 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class BaseManager<D extends BaseEntity, DM extends BaseMapper<D>> extends BaseHandleManager<D, DM> {

    /**
     * 查询数据对象列表
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    public List<D> selectList(D d) {
        return baseMapper.selectList(
                Wrappers.query(d));
    }

    /**
     * 查询数据对象列表 | 附加数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    public List<D> selectListExtra(D d) {
        return baseMapper.selectList(
                Wrappers.query(d));
    }

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    public List<D> selectListByIds(Collection<? extends Serializable> idList) {
        return baseMapper.selectBatchIds(idList);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    public D selectById(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    public D selectByIdExtra(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    public int insert(D d) {
        return baseMapper.insert(d);
    }

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    @DSTransactional
    public int insertBatch(Collection<D> entityList) {
        return baseMapper.insertBatch(entityList);
    }

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    public int update(D d) {
        return baseMapper.updateById(d);
    }

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    @DSTransactional
    public int updateBatch(Collection<D> entityList) {
        return baseMapper.updateBatch(entityList);
    }

    /**
     * 修改状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    public int updateStatus(Serializable id, String status) {
        return baseMapper.update(newBaseObject(),
                Wrappers.<D>update().lambda()
                        .set(D::getStatus, status)
                        .eq(D::getId, id));
    }

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    public int deleteById(Serializable id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 根据Id集合批量删除数据对象
     *
     * @param idList Id集合
     * @return 结果
     */
    public int deleteByIds(Collection<? extends Serializable> idList) {
        return baseMapper.deleteBatchIds(idList);
    }

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 数据对象
     */
    public D checkNameUnique(Serializable id, String name) {
        return baseMapper.selectOne(
                Wrappers.<D>query().lambda()
                        .ne(D::getId, id)
                        .eq(D::getName, name)
                        .last(SqlConstants.LIMIT_ONE));
    }
}
