package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysOrganizeManager {

    @Autowired
    private SysDeptManager deptManager;

    @Autowired
    private SysPostManager postManager;

    @Autowired
    private SysUserPostMergeMapper userPostMergeMapper;

    @Autowired
    private SysRoleDeptMergeMapper roleDeptMergeMapper;

    @Autowired
    private SysRolePostMergeMapper rolePostMergeMapper;

    @Autowired
    private SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    public Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds))
            return new HashSet<>();
        List<SysRoleDeptMerge> roleDeptMerges = roleDeptMergeMapper.selectList(
                Wrappers.<SysRoleDeptMerge>query().lambda()
                        .in(SysRoleDeptMerge::getRoleId, roleIds));
        return roleDeptMerges.stream().map(SysRoleDeptMerge::getDeptId).collect(Collectors.toSet());
    }

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    public Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds))
            return new HashSet<>();
        List<SysRolePostMerge> rolePostMerges = rolePostMergeMapper.selectList(
                Wrappers.<SysRolePostMerge>query().lambda()
                        .in(SysRolePostMerge::getRoleId, roleIds));
        return rolePostMerges.stream().map(SysRolePostMerge::getPostId).collect(Collectors.toSet());
    }

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    public Set<Long> selectUserSetByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds))
            return new HashSet<>();
        List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                Wrappers.<SysUserPostMerge>query().lambda()
                        .in(SysUserPostMerge::getPostId, postIds));
        return userPostMerges.stream().map(SysUserPostMerge::getUserId).collect(Collectors.toSet());
    }

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    public List<SysOrganizeTree> selectOrganizeScope() {
        List<SysDeptDto> deptList = deptManager.selectList(null);
        List<SysPostDto> postList = postManager.selectList(null);
        return new ArrayList<>(CollUtil.addAll(
                postList.stream().map(SysOrganizeTree::new).collect(Collectors.toList()),
                deptList.stream().map(SysOrganizeTree::new).collect(Collectors.toList())));
    }

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    public Long[] selectRoleOrganizeMerge(Long roleId) {
        List<SysRoleDeptMerge> roleDeptMerges = roleDeptMergeMapper.selectList(
                Wrappers.<SysRoleDeptMerge>query().lambda()
                        .eq(SysRoleDeptMerge::getRoleId, roleId));
        List<SysRolePostMerge> rolePostMerges = rolePostMergeMapper.selectList(
                Wrappers.<SysRolePostMerge>query().lambda()
                        .eq(SysRolePostMerge::getRoleId, roleId));
        return CollUtil
                .addAll(roleDeptMerges.stream().map(SysRoleDeptMerge::getDeptId).collect(Collectors.toList()),
                        rolePostMerges.stream().map(SysRolePostMerge::getPostId).collect(Collectors.toList()))
                .toArray(new Long[]{});
    }

    /**
     * 根据部门Id获取关联的角色Ids
     *
     * @param deptId 部门Id
     * @return 角色Ids
     */
    public Long[] selectDeptRoleMerge(Long deptId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getDeptId, deptId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 根据岗位Id获取关联的角色Ids
     *
     * @param postId 岗位Id
     * @return 角色Ids
     */
    public Long[] selectPostRoleMerge(Long postId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getPostId, postId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 根据用户Id获取关联的角色Ids
     *
     * @param userId 用户Id
     * @return 角色Ids
     */
    public Long[] selectUserRoleMerge(Long userId) {
        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                Wrappers.<SysOrganizeRoleMerge>query().lambda()
                        .eq(SysOrganizeRoleMerge::getUserId, userId));
        return organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()).toArray(new Long[]{});
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    public void addRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        List<Long> organizeIdList = new ArrayList<>(Arrays.asList(organizeIds));
        if (CollUtil.isNotEmpty(organizeIdList)) {
            List<SysDeptDto> deptList = deptManager.selectListByIds(organizeIdList);
            if (CollUtil.isNotEmpty(deptList)) {
                // 1.组织Ids中的部门Ids与岗位Ids分开
                List<Long> deptIdList = deptList.stream().map(SysDeptDto::getId).collect(Collectors.toList());
                organizeIdList.removeAll(deptIdList);
                // 2.存储角色与部门的关联数据
                List<SysRoleDeptMerge> roleDeptMerges = deptIdList.stream().map(deptId -> new SysRoleDeptMerge(roleId, deptId)).collect(Collectors.toList());
                roleDeptMergeMapper.insertBatch(roleDeptMerges);
            }
            // 3.存储角色与岗位的关联数据
            List<SysRolePostMerge> rolePostMerges = organizeIdList.stream().map(postId -> new SysRolePostMerge(roleId, postId)).collect(Collectors.toList());
            rolePostMergeMapper.insertBatch(rolePostMerges);
        }
    }


    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    public void editRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        List<Long> organizeIdList = new ArrayList<>(Arrays.asList(organizeIds));
        // 1.校验organizeIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (CollUtil.isNotEmpty(organizeIdList)) {
            // 2.查询organizeIds中的部门Id，分离deptIds与postIds
            List<SysDeptDto> deptList = deptManager.selectListByIds(organizeIdList);
            if (CollUtil.isNotEmpty(deptList)) {
                List<Long> deptIdList = deptList.stream().map(SysDeptDto::getId).collect(Collectors.toList());
                organizeIdList.removeAll(deptIdList);
                // 3.查询原始的角色与部门关联数据,新增/删除差异关联数据
                List<SysRoleDeptMerge> originalDeptList = roleDeptMergeMapper.selectList(
                        Wrappers.<SysRoleDeptMerge>query().lambda()
                                .eq(SysRoleDeptMerge::getRoleId, roleId));
                if (CollUtil.isNotEmpty(originalDeptList)) {
                    List<Long> originalDeptIds = originalDeptList.stream().map(SysRoleDeptMerge::getDeptId).collect(Collectors.toList());
                    List<Long> delDeptIds = new ArrayList<>(originalDeptIds);
                    delDeptIds.removeAll(deptIdList);
                    if (CollUtil.isNotEmpty(delDeptIds)) {
                        roleDeptMergeMapper.delete(
                                Wrappers.<SysRoleDeptMerge>query().lambda()
                                        .eq(SysRoleDeptMerge::getRoleId, roleId)
                                        .in(SysRoleDeptMerge::getDeptId, delDeptIds));
                    }
                    deptIdList.removeAll(originalDeptIds);
                }
                if (CollUtil.isNotEmpty(deptIdList)) {
                    List<SysRoleDeptMerge> roleDeptMerges = deptIdList.stream().map(deptId -> new SysRoleDeptMerge(roleId, deptId)).collect(Collectors.toList());
                    roleDeptMergeMapper.insertBatch(roleDeptMerges);
                }
            }
            // // 4.查询原始的角色与岗位关联数据,新增/删除差异关联数据
            List<SysRolePostMerge> originalPostList = rolePostMergeMapper.selectList(
                    Wrappers.<SysRolePostMerge>query().lambda()
                            .eq(SysRolePostMerge::getRoleId, roleId));
            if (CollUtil.isNotEmpty(originalPostList)) {
                List<Long> originalPostIds = originalPostList.stream().map(SysRolePostMerge::getPostId).collect(Collectors.toList());
                List<Long> delPostIds = new ArrayList<>(originalPostIds);
                delPostIds.removeAll(organizeIdList);
                if (CollUtil.isNotEmpty(delPostIds)) {
                    rolePostMergeMapper.delete(
                            Wrappers.<SysRolePostMerge>query().lambda()
                                    .eq(SysRolePostMerge::getRoleId, roleId)
                                    .in(SysRolePostMerge::getPostId, delPostIds));
                }
                organizeIdList.removeAll(originalPostIds);
            }
            if (CollUtil.isNotEmpty(organizeIdList)) {
                List<SysRolePostMerge> rolePostMerges = organizeIdList.stream().map(postId -> new SysRolePostMerge(roleId, postId)).collect(Collectors.toList());
                rolePostMergeMapper.insertBatch(rolePostMerges);
            }
        } else {
            roleDeptMergeMapper.delete(
                    Wrappers.<SysRoleDeptMerge>query().lambda()
                            .eq(SysRoleDeptMerge::getRoleId, roleId));
            rolePostMergeMapper.delete(
                    Wrappers.<SysRolePostMerge>query().lambda()
                            .in(SysRolePostMerge::getRoleId, roleId));
        }
    }

    /**
     * 修改部门的角色关联数据
     *
     * @param deptId  部门Id
     * @param roleIds 角色Ids
     */
    public void editDeptRoleMerge(Long deptId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getDeptId, deptId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getDeptId, deptId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(deptId, roleId, OrganizeConstants.OrganizeType.DEPT)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getDeptId, deptId));
        }
    }

    /**
     * 修改岗位的角色关联数据
     *
     * @param postId  岗位Id
     * @param roleIds 角色Ids
     */
    public void editPostIdRoleMerge(Long postId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getPostId, postId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getPostId, postId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(postId, roleId, OrganizeConstants.OrganizeType.POST)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getPostId, postId));
        }
    }

    /**
     * 修改用户的角色关联数据
     *
     * @param userId  用户Id
     * @param roleIds 角色Ids
     */
    public void editUserRoleMerge(Long userId, Long[] roleIds) {
        // 校验roleIds是否为空 ? 删除不存在的,增加新增的 : 删除所有
        if (ArrayUtil.isNotEmpty(roleIds)) {
            List<Long> roleIdList = new ArrayList<>(Arrays.asList(roleIds));
            List<SysOrganizeRoleMerge> originalRoleList = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, userId));
            if (CollUtil.isNotEmpty(originalRoleList)) {
                List<Long> originalRoleIds = originalRoleList.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList());
                List<Long> delRoleIds = new ArrayList<>(originalRoleIds);
                delRoleIds.removeAll(roleIdList);
                if (CollUtil.isNotEmpty(delRoleIds)) {
                    organizeRoleMergeMapper.delete(
                            Wrappers.<SysOrganizeRoleMerge>query().lambda()
                                    .eq(SysOrganizeRoleMerge::getUserId, userId)
                                    .in(SysOrganizeRoleMerge::getRoleId, delRoleIds));
                }
                roleIdList.removeAll(originalRoleIds);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SysOrganizeRoleMerge> organizeRoleMerges = roleIdList.stream().map(roleId -> new SysOrganizeRoleMerge(userId, roleId, OrganizeConstants.OrganizeType.USER)).collect(Collectors.toList());
                organizeRoleMergeMapper.insertBatch(organizeRoleMerges);
            }
        } else {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .in(SysOrganizeRoleMerge::getUserId, userId));
        }
    }
}
