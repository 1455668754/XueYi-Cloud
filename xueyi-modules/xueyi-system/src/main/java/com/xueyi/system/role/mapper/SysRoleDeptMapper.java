package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和部门关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleDeptMapper {
    /**
     * 查询部门使用数量
     * 访问控制 rd 租户查询
     *
     * @param search 万用组件 | deptId 部门Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rd")
    public int checkDeptExistRole(SysSearch search);

    /**
     * 批量新增角色和部门关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | deptIds 部门Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRoleDept(SysSearch search);

    /**
     * 删除角色和部门关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptByRoleId(SysSearch search);

    /**
     * 删除角色和部门关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | deptId 部门Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptByDeptId(SysSearch search);

    /**
     * 批量删除角色和部门关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleDeptByIds(SysSearch search);
}
