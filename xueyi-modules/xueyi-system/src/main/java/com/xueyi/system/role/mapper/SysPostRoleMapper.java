package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.role.domain.SysPostRole;
import com.xueyi.system.api.utilTool.SysSearch;

import java.util.List;

/**
 * 岗位和角色关联Mapper接口
 *
 * @author xueyi
 */
public interface SysPostRoleMapper {

    /**
     * 通过岗位Id查询列表
     * 访问控制 pr 租户查询
     *
     * @param search 万用组件 | postId 岗位Id
     * @return 结果
     */
    @DataScope(eAlias = "pr")
    public List<SysPostRole> selectPostRoleByPostId(SysSearch search);

    /**
     * 通过角色Id查询岗位-角色使用数量
     * 访问控制 pr 租户查询
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(eAlias = "pr")
    public int countPostRoleByRoleId(SysSearch search);

    /**
     * 批量新增岗位和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | postId 岗位Id | roleIds 角色Ids
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int batchPostRole(SysSearch search);

    /**
     * 删除岗位和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deletePostRoleByRoleId(SysSearch search);

    /**
     * 删除岗位和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | postId 岗位Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deletePostRoleByPostId(SysSearch search);

    /**
     * 批量删除岗位和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | postIds 需要删除的岗位Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deletePostRoleByIds(SysSearch search);
}
