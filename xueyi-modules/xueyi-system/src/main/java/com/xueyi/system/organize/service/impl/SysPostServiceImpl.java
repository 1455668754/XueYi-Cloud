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
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(SysPost post) {
        // 欲启用岗位时判断归属部门是否启用，未启用则设置本岗位为禁用状态
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", post.getDeptId());
        if (UserConstants.POST_NORMAL.equals(post.getStatus())) {
            SysDept info = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
            if (StringUtils.isNotNull(info) && UserConstants.DEPT_DISABLE.equals(info.getStatus())) {
                post.setStatus(UserConstants.POST_DISABLE);
                try {
                    throw new CustomException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
            }
        }
        return postMapper.insertPost(post);//@param post 岗位信息
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
        // 欲启用岗位时判断归属部门是否启用，未启用则设置本岗位为禁用状态
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", post.getDeptId());
        if (UserConstants.POST_NORMAL.equals(post.getStatus())) {
            SysDept info = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
            if (StringUtils.isNotNull(info) && UserConstants.DEPT_DISABLE.equals(info.getStatus())) {
                post.setStatus(UserConstants.POST_DISABLE);
                try {
                    throw new CustomException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
                updatePostStatus(post.getPostId(), post.getStatus());//修改保存岗位状态
            }
        }
        // 执行岗位状态变更
        return postMapper.updatePost(post);//@param post 岗位信息
    }

    /**
     * 修改保存部门-角色信息
     *
     * @param postId  岗位Id
     * @param roleIds 角色组Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePostRole(Long postId, Long[] roleIds) {
        // 执行部门-角色变更 处理逻辑依次为：1.执行删除 → 2.是否需要执行新增
        SysSearch search = new SysSearch();
        // 删除原有的postRole信息
        search.getSearch().put("postId", postId);
        int rows = postRoleMapper.deletePostRoleByPostId(search);//@param search 查询组件 | postId 岗位Id
        if (roleIds.length > 0) {
            // 改变为最新的postRole信息
            search.getSearch().put("roleIds", roleIds);
            rows = rows + postRoleMapper.batchPostRole(search);//@param search 万用组件 | postId 岗位Id | roleIds 角色Ids
        }
        return rows;
    }

    /**
     * 修改保存岗位状态
     *
     * @param postId 岗位Id
     * @param status 部门状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePostStatus(Long postId, String status) {
        //操作逻辑：当欲设置禁用时，同步执行禁用本岗位所属用户
        int rows;
        SysSearch sear = new SysSearch();
        sear.getSearch().put("postId", postId);
        sear.getSearch().put("status", status);
        // 变更岗位状态
        rows = postMapper.updatePostStatus(sear);//@param search 万用组件 | postId 岗位Id | status 岗位状态
        // 欲停用时停用本岗位所有用户的状态
        if (rows > 0 && UserConstants.POST_DISABLE.equals(status)) {
            rows = rows + userMapper.updateUserStatusByPostId(sear);//@param search 万用组件 | postId 岗位Id | status 用户状态
        }
        return rows;
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位Id
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
