package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和岗位关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRolePostMapper {
    /**
     * 查询岗位使用数量
     * 访问控制 rp 租户查询
     *
     * @param search 万用组件 | postId 岗位Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rp")
    public int checkPostExistRole(SysSearch search);

    /**
     * 批量新增角色和岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | postIds 岗位Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRolePost(SysSearch search);

    /**
     * 删除角色和岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | postId 岗位Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRolePostByPostId(SysSearch search);

    /**
     * 删除角色和岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRolePostByRoleId(SysSearch search);

    /**
     * 批量删除角色和岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRolePostByIds(SysSearch search);
}
