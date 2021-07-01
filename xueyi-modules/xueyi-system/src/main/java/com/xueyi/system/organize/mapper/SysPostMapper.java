package com.xueyi.system.organize.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.organize.SysPost;

/**
 * 岗位信息 数据层
 *
 * @author xueyi
 */
public interface SysPostMapper {
    /**
     * 查询岗位数据集合
     * 访问控制 d 部门 | p 岗位 | p 租户查询
     *
     * @param post 岗位信息
     * @return 岗位数据集合
     */
    @DataScope(deptAlias = "d", postAlias = "p", eAlias = "p")
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 通过岗位Id查询岗位信息
     * 访问控制 p 租户查询
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 角色对象信息
     */
    @DataScope(eAlias = "p")
    public SysPost selectPostById(SysPost post);

    /**
     * 新增岗位信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param post 岗位信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertPost(SysPost post);

    /**
     * 修改岗位信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param post 岗位信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updatePost(SysPost post);

    /**
     * 修改保存岗位状态
     * 访问控制 d 租户查询
     *
     * @param post 岗位信息 | postId 岗位Id | status 岗位状态
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updatePostStatus(SysPost post);

    /**
     * 根据部门Id修改岗位状态（just禁用）
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param post 岗位信息 | deptId 部门Id | status 用户状态
     */
    @DataScope(ueAlias = "empty")
    public int updatePostStatusByDeptId(SysPost post);


    /**
     * 删除岗位信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deletePostById(SysPost post);

    /**
     * 批量删除岗位信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param post 岗位信息 | params.Ids 需要删除的岗位Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deletePostByIds(SysPost post);

    /**
     * 校验部门是否存在岗位
     * 访问控制 p 租户查询
     *
     * @param post 岗位信息 | deptId 部门Id
     * @return 结果
     */
    @DataScope(eAlias = "p")
    public int checkDeptExistPost(SysPost post);

    /**
     * 校验岗位编码
     * 访问控制 p 租户查询
     *
     * @param post 岗位信息 | postCode 岗位编码
     * @return 结果
     */
    @DataScope(eAlias = "p")
    public SysPost checkPostCodeUnique(SysPost post);

    /**
     * 校验岗位名称
     * 访问控制 p 租户查询
     *
     * @param post 岗位信息 | deptId 部门Id | postName 岗位名称
     * @return 结果
     */
    @DataScope(eAlias = "p")
    public SysPost checkPostNameUnique(SysPost post);
}
