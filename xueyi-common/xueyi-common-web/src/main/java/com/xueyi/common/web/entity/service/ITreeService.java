package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.vo.TreeSelect;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层 树型通用数据处理
 *
 * @param <D> Dto
 * @author xueyi
 */
public interface ITreeService<D extends TreeEntity<D>> extends IBaseService<D> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    List<D> selectAncestorsListById(Serializable id);

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    List<D> selectChildListById(Serializable id);

    /**
     * 根据Id及祖籍修改其子节点的状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    int updateChildrenStatus(Serializable id, String status);

    /**
     * 根据Id及旧祖籍修改其子节点的祖籍
     *
     * @param id           Id
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    int updateChildrenAncestors(Serializable id, String newAncestors, String oldAncestors);

    /**
     * 根据Id及旧祖籍修改其子节点的祖籍和状态
     *
     * @param id           Id
     * @param status       状态
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    int updateChildren(Serializable id, String status, String newAncestors, String oldAncestors);

    /**
     * 根据Id及祖籍删除其子节点
     *
     * @param id Id
     * @return 结果
     */
    int deleteChildren(Serializable id);

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    boolean checkIsChild(Serializable id, Serializable parentId);

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    boolean checkHasChild(Serializable id);

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    boolean checkHasNormalChild(Serializable id);

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkNameUnique(Serializable id, Serializable parentId, String name);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param list 数据集合
     * @return 下拉树结构列表
     */
    List<TreeSelect<D>> buildTreeSelect(List<D> list);

}
