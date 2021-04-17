package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和菜单关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleMenuMapper {
    /**
     * 查询菜单使用数量
     * 访问控制 rm 租户查询
     *
     * @param search 万用组件 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "rm")
    public int checkMenuExistRole(SysSearch search);

    /**
     * 批量新增角色和菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id | menuIds 菜单Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int batchRoleMenu(SysSearch search);



    /**
     * 删除角色和菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleMenuByRoleId(SysSearch search);

    /**
     * 删除角色和菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | menuId 菜单Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleMenuByMenuId(SysSearch search);

    /**
     * 批量删除角色和菜单关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteRoleMenuByIds(SysSearch search);
}
