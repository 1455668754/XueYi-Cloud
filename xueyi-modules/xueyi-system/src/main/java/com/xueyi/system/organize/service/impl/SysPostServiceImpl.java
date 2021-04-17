package com.xueyi.system.organize.service.impl;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.role.domain.SysPostRole;
import com.xueyi.system.role.mapper.SysPostRoleMapper;
import com.xueyi.system.api.utilTool.SysSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.organize.SysPost;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 岗位信息 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysPostServiceImpl implements ISysPostService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysPostRoleMapper postRoleMapper;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);//@param post 岗位信息
    }

    /**
     * 通过岗位Id查询岗位信息
     *
     * @param postId 岗位Id
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("postId", postId);
        return postMapper.selectPostById(search);//@param search 万用组件 | postId 岗位Id
    }

    /**
     * 新增保存岗位信息
     * 访问控制 empty 租户更新（无前缀）(SysPostRoleMapper/batchNewPostRole && SysPostMapper/insertPost)
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(updateEnterpriseAlias = "empty")
    public int insertPost(SysPost post) {
        int rows, t;
        SysSearch search = new SysSearch();
        // 1.更新岗位状态 | t==0时代表归属部门被禁用，则该岗位也需变成禁用状态
        t = updatePostStatus(post.getPostId(), post.getDeptId(), post.getStatus());
        if (t == 0) {
            post.setStatus(UserConstants.POST_DISABLE);
        }
        rows = postMapper.insertPost(post);//@param post 岗位信息
        if (rows > 0 && post.getRoleIds().length > 0) {
            search.getSearch().put("roleIds", post.getRoleIds());
            rows = rows + postRoleMapper.batchNewPostRole(search);//@param search 万用组件 | 自动生成Id做postId | roleIds 角色Ids
        }
        return rows;
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePost(SysPost post) {
        int rows, t;
        SysSearch search = new SysSearch();
        // 1.更新岗位状态 | t==0时代表归属部门被禁用，则该岗位也需变成禁用状态
        t = updatePostStatus(post.getPostId(), post.getDeptId(), post.getStatus());
        if (t == 0) {
            post.setStatus(UserConstants.POST_DISABLE);
        }
        rows = postMapper.updatePost(post);//@param post 岗位信息
        //执行岗位-角色变更 处理逻辑依次为：1.判断是否变动 → 2.是否需要执行删除 → 3.是否需要执行新增
        if (rows > 0 && post.getRoleIds().length > 0) {
            search.getSearch().put("postId", post.getPostId());
            List<SysPostRole> postRoles = postRoleMapper.selectPostRoleByPostId(search);//@param search 万用组件 | postId 岗位Id
            int k = 0;
            //1.检验roles是否变动
            if (postRoles.size() > 0) {
                for (SysPostRole postRole : postRoles) {
                    for (Long role : post.getRoleIds()) {
                        if (role.equals(postRole.getRoleId())) {
                            k++;
                        }
                    }
                }
            }
            if (k != postRoles.size() && k != post.getRoleIds().length) {
                //2.删除原有的postRole信息
                search.getSearch().put("postId", post.getPostId());
                search.getSearch().put("roleIds", post.getRoleIds());
                postRoleMapper.deletePostRoleByPostId(search);//@param search 查询组件 | postId 岗位Id
                //3.改变为最新的postRole信息
                rows = rows + postRoleMapper.batchPostRole(search);//@param search 万用组件 | postId 岗位Id | roleIds 角色Ids
            }
        }
        return rows;
    }

    /**
     * 修改保存岗位状态
     *
     * @param postId 岗位Id
     * @param deptId 部门Id
     * @param status 部门状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePostStatus(Long postId, Long deptId, String status) {
        int rows = 1;
        SysSearch sear = new SysSearch();
        // 1.当部门状态为禁用时，停用该岗位
        if (UserConstants.POST_NORMAL.equals(status)) {
            sear.getSearch().put("deptId", deptId);
            SysDept dept = deptMapper.selectDeptById(sear);//@param search 万用组件 | deptId 部门Id
            if (UserConstants.DEPT_DISABLE.equals(dept.getStatus())) {
                status = UserConstants.POST_DISABLE;
                try {
                    rows = 0;
                    throw new CustomException(String.format("%1$s已停用,无法启用该岗位", dept.getDeptName()));
                } catch (Exception ignored) {
                }
            }
        }
        if (StringUtils.isNotNull(postId)) {
            sear.getSearch().put("postId", postId);
            sear.getSearch().put("status", status);
            // 2.停用时停用该岗位所有用户
            if (UserConstants.POST_DISABLE.equals(status)) {
                rows = userMapper.updateUserStatusByPostId(sear);//@param search 万用组件 | postId 岗位Id | status 用户状态
            }
            rows = postMapper.updatePostStatus(sear);//@param search 万用组件 | postId 岗位Id | status 岗位状态
        }
        return rows;
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePostById(Long postId) {
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("postId", postId);
        rows = postMapper.deletePostById(search);//@param search 万用组件 | postId 岗位Id
        if (rows > 0) {
            rows = rows + postRoleMapper.deletePostRoleByPostId(search);//@param search 查询组件 | postId 岗位Id
        }
        return rows;
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位Ids
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("postIds", postIds);
        rows = postMapper.deletePostByIds(search);//@param search 万用组件 | postIds 需要删除的岗位Ids(Long[])
        if (rows > 0) {
            rows = rows + postRoleMapper.deletePostRoleByIds(search);//@param search 查询组件 | postIds 需要删除的岗位Ids(Long[])
        }
        return rows;
    }

    /**
     * 校验岗位是否存在用户
     *
     * @param postId 岗位Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkPostExistUser(Long postId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("postId", postId);
        int result = userMapper.checkPostExistUser(search);//@param search 万用组件 | postId 岗位Id
        return result > 0;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param postId   岗位Id
     * @param postCode 岗位编码
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(Long postId, String postCode) {
        if (StringUtils.isNull(postId)) {
            postId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("postCode", postCode);
        SysPost info = postMapper.checkPostCodeUnique(search);//@param search 万用组件 | postCode 岗位编码
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param postId   岗位Id
     * @param deptId   部门Id
     * @param postName 岗位名称
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(Long postId, Long deptId, String postName) {
        if (StringUtils.isNull(postId)) {
            postId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        search.getSearch().put("postName", postName);
        SysPost info = postMapper.checkPostNameUnique(search);//@param search 万用组件 | deptId 部门Id | postName 岗位名称
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
