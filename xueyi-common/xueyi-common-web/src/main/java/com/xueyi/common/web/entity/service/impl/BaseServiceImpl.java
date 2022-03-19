package com.xueyi.common.web.entity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.impl.handle.BaseHandleServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 服务层 基类实现通用数据处理
 *
 * @param <D>  Dto
 * @param <DG> DtoManager
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class BaseServiceImpl<D extends BaseEntity, DG extends BaseManager<D, DM>, DM extends BaseMapper<D>> extends BaseHandleServiceImpl<D, DG, DM> implements IBaseService<D> {

    /**
     * 查询数据对象列表
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectList(D d) {
        return baseManager.selectList(d);
    }

    /**
     * 查询数据对象列表 | 附加数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListExtra(D d) {
        return baseManager.selectListExtra(d);
    }

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param d 数据对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListScope(D d) {
        return baseManager.selectListExtra(d);
    }

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListByIds(Collection<? extends Serializable> idList) {
        return baseManager.selectListByIds(idList);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectById(Serializable id) {
        return baseManager.selectById(id);
    }

    /**
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdExtra(Serializable id) {
        return baseManager.selectByIdExtra(id);
    }

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int insert(D d) {
        return baseManager.insert(d);
    }

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    @Override
    public int insertBatch(Collection<D> entityList) {
        return baseManager.insertBatch(entityList);
    }

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int update(D d) {
        return baseManager.update(d);
    }

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    @Override
    public int updateBatch(Collection<D> entityList) {
        return baseManager.updateBatch(entityList);
    }

    /**
     * 修改数据对象状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateStatus(Serializable id, String status) {
        return baseManager.updateStatus(id, status);
    }

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        return baseManager.deleteById(id);
    }

    /**
     * 根据Id集合删除数据对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        return baseManager.deleteByIds(idList);
    }

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkNameUnique(Serializable id, String name) {
        return ObjectUtil.isNotNull(baseManager.checkNameUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, name));
    }

    /**
     * 根据Id查询数据对象状态
     *
     * @param id Id
     * @return 结果 | NORMAL 正常 | DISABLE 停用 | EXCEPTION 异常（值不存在）
     */
    @Override
    public BaseConstants.Status checkStatus(Serializable id) {
        D info = StringUtils.isNotNull(id) ? baseManager.selectById(id) : null;
        return ObjectUtil.isNull(info)
                ? BaseConstants.Status.EXCEPTION
                : StrUtil.equals(BaseConstants.Status.NORMAL.getCode(), info.getStatus())
                ? BaseConstants.Status.NORMAL
                : BaseConstants.Status.DISABLE;
    }
}