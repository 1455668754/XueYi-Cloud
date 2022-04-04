package com.xueyi.system.organize.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.SysOrganizeManager;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 组织管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysOrganizeServiceImpl implements ISysOrganizeService {

    @Autowired
    private SysOrganizeManager organizeManager;

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    @Override
    public Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds) {
        return organizeManager.selectRoleDeptSetByRoleIds(roleIds);
    }

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    @Override
    public Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds) {
        return organizeManager.selectRolePostSetByRoleIds(roleIds);
    }

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    @Override
    public Set<Long> selectUserSetByPostIds(Collection<Long> postIds) {
        return organizeManager.selectUserSetByPostIds(postIds);
    }

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        return organizeManager.selectOrganizeScope();
    }

    /**
     * 获取企业部门|岗位树 | 移除无归属岗位的部门叶子节点
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeTreeExDeptNode() {
        List<SysOrganizeTree> organizeTrees = TreeUtils.buildTree(organizeManager.selectOrganizeScope());
        recursionDelLeaf(organizeTrees);
        return organizeTrees;
    }

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    @Override
    public Long[] selectRoleOrganizeMerge(Long roleId) {
        return organizeManager.selectRoleOrganizeMerge(roleId);
    }

    /**
     * 根据部门Id获取关联的角色Ids
     *
     * @param deptId 部门Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectDeptRoleMerge(Long deptId) {
        return organizeManager.selectDeptRoleMerge(deptId);
    }

    /**
     * 根据岗位Id获取关联的角色Ids
     *
     * @param postId 岗位Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectPostRoleMerge(Long postId) {
        return organizeManager.selectPostRoleMerge(postId);
    }

    /**
     * 根据用户Id获取关联的角色Ids
     *
     * @param userId 用户Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectUserRoleMerge(Long userId) {
        return organizeManager.selectUserRoleMerge(userId);
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void addRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        organizeManager.addRoleOrganizeMerge(roleId, organizeIds);
    }


    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void editRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        organizeManager.editRoleOrganizeMerge(roleId, organizeIds);
    }

    /**
     * 修改部门的角色关联数据
     *
     * @param deptId  部门Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editDeptRoleMerge(Long deptId, Long[] roleIds) {
        organizeManager.editDeptRoleMerge(deptId, roleIds);
    }

    /**
     * 修改岗位的角色关联数据
     *
     * @param postId  岗位Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editPostIdRoleMerge(Long postId, Long[] roleIds) {
        organizeManager.editPostIdRoleMerge(postId, roleIds);
    }

    /**
     * 修改用户的角色关联数据
     *
     * @param userId  用户Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editUserRoleMerge(Long userId, Long[] roleIds) {
        organizeManager.editUserRoleMerge(userId, roleIds);
    }

    /**
     * 递归列表 | 移除无归属岗位的部门叶子节点
     */
    private void recursionDelLeaf(List<SysOrganizeTree> treeList) {
        SysOrganizeTree treeNode;
        for (int i = treeList.size() - 1; i >= 0; i--) {
            treeNode = treeList.get(i);
            if (CollUtil.isNotEmpty(treeNode.getChildren()))
                recursionDelLeaf(treeNode.getChildren());
            if (StrUtil.equals(treeNode.getType(), OrganizeConstants.OrganizeType.DEPT.getCode())
                    && CollUtil.isEmpty(treeNode.getChildren()))
                treeList.remove(i);
        }
    }
}
