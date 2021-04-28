package com.xueyi.system.organize.service;

import java.util.List;

import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.api.organize.SysPost;
import com.xueyi.system.organize.domain.deptPostVo;
import com.xueyi.system.utils.vo.TreeSelect;

/**
 * 岗位信息 服务层
 *
 * @author xueyi
 */
public interface ISysPostService {
    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 通过岗位Id查询岗位信息
     *
     * @param postId 岗位Id
     * @return 角色对象信息
     */
    public SysPost selectPostById(Long postId);

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int insertPost(SysPost post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(SysPost post);

    /**
     * 修改保存部门-角色信息
     *
     * @param postId  岗位Id
     * @param roleIds 角色组Ids
     * @return 结果
     */
    public int updatePostRole(Long postId, Long[] roleIds);

    /**
     * 修改保存岗位状态
     *
     * @param postId 岗位Id
     * @param status 部门状态
     * @return 结果
     */
    public int updatePostStatus(Long postId, String status);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位Id
     * @return 结果
     */
    public int deletePostById(Long postId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位Id
     * @return 结果
     * @throws Exception 异常
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * 校验岗位是否存在用户
     *
     * @param postId 岗位Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkPostExistUser(Long postId);

    /**
     * 校验岗位编码
     *
     * @param postId   岗位Id
     * @param postCode 岗位编码
     * @return 结果
     */
    public String checkPostCodeUnique(Long postId, String postCode);

    /**
     * 校验岗位名称
     *
     * @param postId   岗位Id
     * @param deptId   部门Id
     * @param postName 岗位名称
     * @return 结果
     */
    public String checkPostNameUnique(Long postId, Long deptId, String postName);

    /**
     * 校验岗位状态
     *
     * @param postId 岗位Id
     * @return 结果
     */
    public String checkPostStatus(Long postId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptPostTreeSelect();

    /**
     * 构建前端所需要树结构
     *
     * @param deptPostList 部门-岗位数组装列表
     * @return 树结构列表
     */
    public List<deptPostVo> buildDeptPostTree(List<deptPostVo> deptPostList);
}
