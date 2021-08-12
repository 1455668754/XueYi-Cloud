package com.xueyi.system.role.service;

import java.util.List;

import com.xueyi.system.api.domain.role.SysRoleSystemMenu;

/**
 * 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysRoleSystemMenuService {

    /**
     * 获取指定企业账号的租管衍生角色菜单范围信息 | 租管系统使用方法
     *
     * @param systemMenu 角色和系统关联对象 | sourceName 指定源 | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> getEnterpriseMenuScopeById(SysRoleSystemMenu systemMenu);

    /**
     * 修改保存指定企业账号的租管衍生角色菜单权限 | 租管系统使用方法
     *
     * @param systemMenu 角色和系统关联对象 | sourceName 指定源 | params.systemMenuIds 模块&菜单Id集合 | enterpriseId 租户Id
     * @return 结果
     */
    public int authMenuScopeById(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取超管衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitAdministrator(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取租户衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitEnterprise(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitPersonalScreenDerive(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitPersonal(SysRoleSystemMenu systemMenu);
}
