package com.xueyi.common.web.entity.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.common.web.entity.manager.handle.SubBaseHandleManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 数据封装层 主子基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @param <S>  SubDto
 * @param <SM> SubMapper
 * @author xueyi
 */
public abstract class SubBaseManager<D extends SubBaseEntity<S>, DM extends SubBaseMapper<D, S>, S extends BaseEntity, SM extends BaseMapper<S>> extends SubBaseHandleManager<D, DM, S, SM> {

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    // 待优化 联表更新后
    @Override
    public List<D> selectListExtra(D d) {
        List<D> list = baseMapper.selectList(Wrappers.query(d));
        list.forEach(item -> {
            LambdaQueryWrapper<S> subQueryWrapper = new LambdaQueryWrapper<>();
            querySetForeignKey(subQueryWrapper, item);
            item.setSubList(subMapper.selectList(subQueryWrapper));
        });
        return list;
    }

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdExtra(Serializable id) {
        D d = baseMapper.selectById(id);
        LambdaQueryWrapper<S> subQueryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(subQueryWrapper, d);
        d.setSubList(subMapper.selectList(subQueryWrapper));
        return d;
    }

    /**
     * 根据外键查询子数据对象集合 | 子数据
     *
     * @param foreignKey 外键
     * @return 子数据对象集合
     */
    public List<S> selectSubByForeignKey(Serializable foreignKey) {
        LambdaQueryWrapper<S> subQueryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(subQueryWrapper, foreignKey);
        return subMapper.selectList(subQueryWrapper);
    }

    /**
     * 根据Id修改其归属数据的状态
     *
     * @param foreignKey 子表外键值
     * @param status     状态
     * @return 结果
     */
    public int updateSubStatus(Serializable foreignKey, String status) {
        LambdaUpdateWrapper<S> updateWrapper = new LambdaUpdateWrapper<>();
        updateSetForeignKey(updateWrapper, foreignKey);
        updateWrapper
                .set(S::getStatus, status);
        return subMapper.delete(updateWrapper);
    }

    /**
     * 根据Id删除其归属数据
     *
     * @param foreignKey 子表外键值
     * @return 结果
     */
    public int deleteSub(Serializable foreignKey) {
        LambdaUpdateWrapper<S> deleteWrapper = new LambdaUpdateWrapper<>();
        updateSetForeignKey(deleteWrapper, foreignKey);
        return subMapper.delete(deleteWrapper);
    }

    /**
     * 校验是否存在子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    public S checkExistSub(Serializable foreignKey) {
        LambdaQueryWrapper<S> queryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(queryWrapper, foreignKey);
        queryWrapper
                .last(SqlConstants.LIMIT_ONE);
        return subMapper.selectOne(queryWrapper);
    }

    /**
     * 校验是否存在启用（正常状态）的子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    public S checkExistNormalSub(Serializable foreignKey) {
        LambdaQueryWrapper<S> queryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(queryWrapper, foreignKey);
        queryWrapper
                .eq(S::getStatus, BaseConstants.Status.NORMAL.getCode())
                .last(SqlConstants.LIMIT_ONE);
        return subMapper.selectOne(queryWrapper);
    }

}
