package com.xueyi.common.web.entity.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.manager.handle.TreeHandleManager;
import com.xueyi.common.web.entity.mapper.TreeMapper;

import java.io.Serializable;
import java.util.List;

import static com.xueyi.common.core.constant.basic.SqlConstants.ANCESTORS_FIND;

/**
 * 数据封装层 树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class TreeManager<D extends TreeEntity<D>, DM extends TreeMapper<D>> extends TreeHandleManager<D, DM> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    public List<D> selectAncestorsListById(Serializable id) {
        D d = baseMapper.selectById(id);
        if (ObjectUtil.isNull(d) || StrUtil.isBlank(d.getAncestors()))
            return null;
        return baseMapper.selectList(
                Wrappers.<D>query().lambda()
                        .eq(D::getId, id)
                        .or().in(D::getId, StrUtil.splitTrim(d.getAncestors(), ",")));
    }

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    public List<D> selectChildListById(Serializable id) {
        return baseMapper.selectList(
                Wrappers.<D>query().lambda()
                        .eq(D::getId, id)
                        .or().apply(ANCESTORS_FIND, id));
    }

    /**
     * 根据Id修改其子节点的状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    public int updateChildrenStatus(Serializable id, String status) {
        return baseMapper.update(newBaseObject(),
                Wrappers.<D>update().lambda()
                        .set(D::getStatus, status)
                        .apply(ANCESTORS_FIND, id));
    }

    /**
     * 根据Id修改其子节点的祖籍
     *
     * @param id           Id
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    public int updateChildrenAncestors(Serializable id, String newAncestors, String oldAncestors) {
        return baseMapper.update(newBaseObject(),
                Wrappers.<D>update().lambda()
                        .setSql(StrUtil.format("{} = insert({},{},{},'{}')", SqlConstants.Entity.ANCESTORS.getCode(), SqlConstants.Entity.ANCESTORS.getCode(), 1, oldAncestors.length(), newAncestors))
                        .apply(ANCESTORS_FIND, id));
    }

    /**
     * 根据Id修改其子节点的祖籍和状态
     *
     * @param id           Id
     * @param status       状态
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    public int updateChildren(Serializable id, String status, String newAncestors, String oldAncestors) {
        return baseMapper.update(newBaseObject(),
                Wrappers.<D>update().lambda()
                        .set(D::getStatus, status)
                        .setSql(StrUtil.format("{} = insert({},{},{},{})", SqlConstants.Entity.ANCESTORS.getCode(), SqlConstants.Entity.ANCESTORS.getCode(), 1, oldAncestors.length(), newAncestors))
                        .apply(ANCESTORS_FIND, id));
    }

    /**
     * 根据Id删除其子节点
     *
     * @param id Id
     * @return 结果
     */
    public int deleteChildren(Serializable id) {
        return baseMapper.delete(
                Wrappers.<D>update().lambda()
                        .eq(D::getId, id)
                        .apply(ANCESTORS_FIND, id));
    }

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    public D checkIsChild(Serializable id, Serializable parentId) {
        return baseMapper.selectOne(
                Wrappers.<D>query().lambda()
                        .eq(D::getId, id)
                        .apply(ANCESTORS_FIND, parentId)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    public D checkHasChild(Serializable id) {
        return baseMapper.selectOne(
                Wrappers.<D>query().lambda()
                        .apply(ANCESTORS_FIND, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    public D checkHasNormalChild(Serializable id) {
        return baseMapper.selectOne(
                Wrappers.<D>query().lambda()
                        .eq(D::getStatus, BaseConstants.Status.NORMAL.getCode())
                        .apply(ANCESTORS_FIND, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 数据对象
     */
    public D checkNameUnique(Serializable id, Serializable parentId, String name) {
        return baseMapper.selectOne(
                Wrappers.<D>query().lambda()
                .ne(D::getId, id)
                .eq(D::getParentId, parentId)
                .eq(D::getName, name)
                .last(SqlConstants.LIMIT_ONE));
    }
}
