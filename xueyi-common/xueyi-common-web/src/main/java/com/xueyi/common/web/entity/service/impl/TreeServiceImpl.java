package com.xueyi.common.web.entity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.vo.TreeSelect;
import com.xueyi.common.web.entity.manager.TreeManager;
import com.xueyi.common.web.entity.mapper.TreeMapper;
import com.xueyi.common.web.entity.service.ITreeService;
import com.xueyi.common.web.entity.service.impl.handle.TreeHandleServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务层 树型实现通用数据处理
 *
 * @param <D>  Dto
 * @param <DG> DtoManager
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class TreeServiceImpl<D extends TreeEntity<D>, DG extends TreeManager<D, DM>, DM extends TreeMapper<D>> extends TreeHandleServiceImpl<D, DG, DM> implements ITreeService<D> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    @Override
    public List<D> selectAncestorsListById(Serializable id) {
        return baseManager.selectAncestorsListById(id);
    }

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    @Override
    public List<D> selectChildListById(Serializable id) {
        return baseManager.selectChildListById(id);
    }

    /**
     * 新增修改数据对象
     * 同步启用父节点
     *
     * @param d 数据对象
     * @return 结果
     * @see #AUHandleParentStatusCheck(Serializable, String) 树型 检查父级状态
     * @see #AHandleAncestorsSet(TreeEntity) 树型 设置祖籍
     */
    @Override
    @DSTransactional
    public int insert(D d) {
        AUHandleParentStatusCheck(d.getParentId(), d.getStatus());
        AHandleAncestorsSet(d);
        return baseManager.insert(d);
    }

    /**
     * 修改数据对象
     * 同步 修改子节点祖籍 || 停用子节点 || 启用父节点
     *
     * @param d 数据对象
     * @return 结果
     * @see #AUHandleParentStatusCheck(Serializable, String) 树型 检查父级状态
     * @see #UHandleAncestorsCheck(TreeEntity) 树型 检验祖籍是否变更
     * @see #UUSChildrenStatusCheck(Serializable, String) 树型 检查子节点状态
     */
    @Override
    @DSTransactional
    public int update(D d) {
        AUHandleParentStatusCheck(d.getParentId(), d.getStatus());
        UHandleAncestorsCheck(d);
        UUSChildrenStatusCheck(d.getId(), d.getStatus());
        return baseManager.update(d);
    }

    /**
     * 修改数据对象状态
     * 同步停用子节点 || 启用父节点
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     * @see #USHandelParentStatusCheck(Serializable, String) 树型 检查父级状态
     * @see #UUSChildrenStatusCheck(Serializable, String) 树型 检查子节点状态
     */
    @Override
    @DSTransactional
    public int updateStatus(Serializable id, String status) {
        USHandelParentStatusCheck(id, status);
        UUSChildrenStatusCheck(id, status);
        return baseManager.updateStatus(id, status);
    }

    /**
     * 根据Id删除数据对象
     * 同步删除 子节点
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        baseManager.deleteChildren(id);
        return baseManager.deleteById(id);
    }

    /**
     * 根据Id集合删除数据对象
     * 同步删除 子节点
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        for (Serializable id : idList)
            baseManager.deleteChildren(id);
        return baseManager.deleteByIds(idList);
    }

    /**
     * 根据Id修改其子节点的状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateChildrenStatus(Serializable id, String status) {
        return baseManager.updateChildrenStatus(id, status);
    }

    /**
     * 根据Id修改其子节点的祖籍
     *
     * @param id           Id
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    @Override
    public int updateChildrenAncestors(Serializable id, String newAncestors, String oldAncestors) {
        return baseManager.updateChildrenAncestors(id, newAncestors, oldAncestors);
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
    @Override
    public int updateChildren(Serializable id, String status, String newAncestors, String oldAncestors) {
        return baseManager.updateChildren(id, status, newAncestors, oldAncestors);
    }

    /**
     * 根据Id删除其子节点
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteChildren(Serializable id) {
        return baseManager.deleteChildren(id);
    }

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public boolean checkIsChild(Serializable id, Serializable parentId) {
        return ObjectUtil.isNotNull(baseManager.checkIsChild(id, parentId));
    }

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkHasChild(Serializable id) {
        return ObjectUtil.isNotNull(baseManager.checkHasChild(id));
    }

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkHasNormalChild(Serializable id) {
        return ObjectUtil.isNotNull(baseManager.checkHasNormalChild(id));
    }

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkNameUnique(Serializable id, Serializable parentId, String name) {
        return ObjectUtil.isNotNull(baseManager.checkNameUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id,
                ObjectUtil.isNull(parentId) ? BaseConstants.NONE_ID : parentId, name));
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param list 数据集合
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect<D>> buildTreeSelect(List<D> list) {
        List<D> deptTrees = TreeUtils.buildTree(list);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}
