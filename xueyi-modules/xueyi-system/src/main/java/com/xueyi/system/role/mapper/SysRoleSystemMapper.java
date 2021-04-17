package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和系统关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleSystemMapper {
    /**
     * 查询系统使用数量
     * 访问控制 rs 租户查询
     *
     * @param search 万用组件 | systemId 系统Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rs")
    public int checkSystemExistRole(SysSearch search);

    /**
     * 批量新增角色和系统关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | systemIds 系统Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRoleSystem(SysSearch search);

    /**
     * 删除角色和系统关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemByRoleId(SysSearch search);

    /**
     * 删除角色和系统关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | systemId 系统Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemBySystemId(SysSearch search);

    /**
     * 批量删除角色和系统关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemByIds(SysSearch search);
}
