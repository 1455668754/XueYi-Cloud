package com.xueyi.system.organize.service;

import java.util.List;

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
     * @param post 岗位信息 | postId 岗位Id
     * @return 角色对象信息
     */
    public SysPost selectPostById(SysPost post);

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
     * @param post 岗位信息 | postId  岗位Id | roleIds 角色组Ids
     * @return 结果
     */
    public int updatePostRole(SysPost post);

    /**
     * 修改保存岗位状态
     *
     * @param post 岗位信息 | postId 岗位Id | status 部门状态
     * @return 结果
     */
    public int updatePostStatus(SysPost post);

    /**
     * 删除岗位信息
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果
     */
    public int deletePostById(SysPost post);

    /**
     * 批量删除岗位信息
     *
     * @param post 岗位信息 | params.Ids 需要删除的岗位Ids组
     * @return 结果
     * @throws Exception 异常
     */
    public int deletePostByIds(SysPost post);

    /**
     * 校验岗位是否存在用户
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkPostExistUser(SysPost post);

    /**
     * 校验岗位编码
     *
     * @param post 岗位信息 | postId   岗位Id | postCode 岗位编码
     * @return 结果
     */
    public String checkPostCodeUnique(SysPost post);

    /**
     * 校验岗位名称
     *
     * @param post 岗位信息 | postId   岗位Id | deptId   部门Id | postName 岗位名称
     * @return 结果
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * 校验岗位状态
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果
     */
    public String checkPostStatus(SysPost post);

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
