package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和部门-岗位关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleDeptPostMapper {
    /**
     * 查询部门-岗位使用数量
     * 访问控制 rdp 租户查询
     *
     * @param search 万用组件 | deptPostId 部门-岗位Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rdp")
    public int checkDeptPostExistRole(SysSearch search);

    /**
     * 批量新增角色和部门-岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | deptPostIds 部门Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRoleDeptPost(SysSearch search);

    /**
     * 删除角色和部门-岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptPostByRoleId(SysSearch search);

    /**
     * 删除角色和部门-岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | deptPostId 部门-岗位Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptPostByDeptPostId(SysSearch search);

    /**
     * 批量删除角色和部门-岗位关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptPostByIds(SysSearch search);
}