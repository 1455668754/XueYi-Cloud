package com.xueyi.system.role.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.role.domain.SysUserRole;

/**
 * 用户和角色关联Mapper接口
 *
 * @author xueyi
 */
public interface SysUserRoleMapper {

    /**
     * 通过用户Id查询列表
     * 访问控制 ur 租户查询
     *
     * @param search 万用组件 | userId 用户Id
     * @return 结果
     */
    @DataScope(eAlias = "ur")
    public List<SysUserRole> selectUserRoleByUserId(SysSearch search);

    /**
     * 通过角色Id查询用户-角色使用数量
     * 访问控制 ur 租户查询
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(eAlias = "ur")
    public int countUserRoleByRoleId(SysSearch search);

    /**
     * 批量新增用户和角色关联
     * 访问控制 empty 租户更新（无前缀）(控制器在SysUserServiceImpl层 insertUser方法)
     *
     * @param search 万用组件 | 自动生成Id做userId | roleIds 角色Ids
     * @return 结果
     */
    public int batchNewUserRole(SysSearch search);

    /**
     * 批量新增用户和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userId 用户Id | roleIds 角色Ids
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int batchUserRole(SysSearch search);

    /**
     * 删除用户和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteUserRoleByRoleId(SysSearch search);

    /**
     * 删除用户和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | userId 用户Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteUserRoleByUserId(SysSearch search);

    /**
     * 批量删除用户和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | userIds 需要删除的用户Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteUserRoleByIds(SysSearch search);
}
