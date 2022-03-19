package com.xueyi.common.web.entity.manager.handle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 数据封装层 操作方法 主子基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @param <S>  SubDto
 * @param <SM> SubMapper
 * @author xueyi
 */
public abstract class SubBaseHandleManager<D extends SubBaseEntity<S>, DM extends SubBaseMapper<D, S>, S extends BaseEntity, SM extends BaseMapper<S>> extends BaseManager<D, DM> {

    @Autowired
    protected SM subMapper;

    /**
     * 查询 设置主子表中子表外键值
     *
     * @param queryWrapper 条件构造器
     * @param d            数据对象
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#selectByIdExtra(Serializable)
     */
    protected void querySetForeignKey(LambdaQueryWrapper<S> queryWrapper, D d) {
        setForeignKey(queryWrapper, null, d, null);
    }

    /**
     * 查询 设置主子表中子表外键值
     *
     * @param queryWrapper 条件构造器
     * @param foreignKey   子表外键值
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#checkExistSub(Serializable)
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#checkExistNormalSub(Serializable)
     */
    protected void querySetForeignKey(LambdaQueryWrapper<S> queryWrapper, Serializable foreignKey) {
        setForeignKey(queryWrapper, null, null, foreignKey);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param updateWrapper 条件构造器
     * @param d             数据对象
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#updateSubStatus(Serializable, String)
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#deleteSub(Serializable)
     */
    protected void updateSetForeignKey(LambdaUpdateWrapper<S> updateWrapper, D d) {
        setForeignKey(null, updateWrapper, d, null);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param updateWrapper 条件构造器
     * @param foreignKey    子表外键值
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#updateSubStatus(Serializable, String)
     * @see com.xueyi.common.web.entity.manager.SubTreeManager#deleteSub(Serializable)
     */
    protected void updateSetForeignKey(LambdaUpdateWrapper<S> updateWrapper, Serializable foreignKey) {
        setForeignKey(null, updateWrapper, null, foreignKey);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param queryWrapper  条件构造器
     * @param updateWrapper 条件构造器
     * @param d             数据对象
     * @param foreignKey    子表外键值
     */
    protected abstract void setForeignKey(LambdaQueryWrapper<S> queryWrapper, LambdaUpdateWrapper<S> updateWrapper, D d, Serializable foreignKey);
}
