package com.xueyi.system.role.service;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.role.domain.SysRoleSystemMenu;

/**
 * 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysRoleSystemMenuService {

    /**
     * 仅获取超管衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitAdministrator(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取租户衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitEnterprise(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitPersonalScreenDerive(SysRoleSystemMenu systemMenu);

    /**
     * 仅获取个人衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    public List<SysRoleSystemMenu> selectPermitPersonal(SysRoleSystemMenu systemMenu);
}
