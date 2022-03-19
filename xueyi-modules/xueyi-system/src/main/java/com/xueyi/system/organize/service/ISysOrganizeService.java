package com.xueyi.system.organize.service;

import com.xueyi.system.organize.domain.vo.SysOrganizeTree;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 组织管理 服务层
 *
 * @author xueyi
 */
public interface ISysOrganizeService {

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds);

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds);

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    Set<Long> selectUserSetByPostIds(Collection<Long> postIds);

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    List<SysOrganizeTree> selectOrganizeScope();

    /**
     * 获取企业部门|岗位树 | 移除无归属岗位的部门叶子节点
     *
     * @return 组织对象集合
     */
    List<SysOrganizeTree> selectOrganizeTreeExDeptNode();

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    Long[] selectRoleOrganizeMerge(Long roleId);

    /**
     * 根据部门Id获取关联的角色Ids
     *
     * @param deptId 部门Id
     * @return 角色Ids
     */
    Long[] selectDeptRoleMerge(Long deptId);

    /**
     * 根据岗位Id获取关联的角色Ids
     *
     * @param postId 岗位Id
     * @return 角色Ids
     */
    Long[] selectPostRoleMerge(Long postId);

    /**
     * 根据用户Id获取关联的角色Ids
     *
     * @param userId 用户Id
     * @return 角色Ids
     */
    Long[] selectUserRoleMerge(Long userId);

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    void addRoleOrganizeMerge(Long roleId, Long[] organizeIds);

    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    void editRoleOrganizeMerge(Long roleId, Long[] organizeIds);

    /**
     * 修改部门的角色关联数据
     *
     * @param deptId  部门Id
     * @param roleIds 角色Ids
     */
    void editDeptRoleMerge(Long deptId, Long[] roleIds);

    /**
     * 修改岗位的角色关联数据
     *
     * @param postId  岗位Id
     * @param roleIds 角色Ids
     */
    void editPostIdRoleMerge(Long postId, Long[] roleIds);

    /**
     * 修改用户的角色关联数据
     *
     * @param userId  用户Id
     * @param roleIds 角色Ids
     */
    void editUserRoleMerge(Long userId, Long[] roleIds);
}
