package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和系统-菜单关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleSystemMenuMapper {
    /**
     * 查询系统-菜单使用数量
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rsm")
    public int checkSystemMenuExistRole(SysSearch search);

    /**
     * 批量新增角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | systemMenuIds 系统-菜单Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRoleSystemMenu(SysSearch search);

    /**
     * 删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemMenuByRoleId(SysSearch search);

    /**
     * 删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemMenuBySystemMenuId(SysSearch search);

    /**
     * 批量删除角色和系统-菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleSystemMenuByIds(SysSearch search);
}
