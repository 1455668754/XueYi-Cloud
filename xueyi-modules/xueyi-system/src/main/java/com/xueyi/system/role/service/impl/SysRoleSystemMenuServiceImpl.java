package com.xueyi.system.role.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.role.domain.SysRoleSystemMenu;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SysRoleSystemMenuServiceImpl implements ISysRoleSystemMenuService {

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    /**
     * 仅获取超管衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitAdministrator(SysRoleSystemMenu systemMenu){
        return roleSystemMenuMapper.selectPermitAdministrator(systemMenu);
    }

    /**
     * 仅获取租户衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitEnterprise(SysRoleSystemMenu systemMenu){
        return roleSystemMenuMapper.selectPermitEnterprise(systemMenu);
    }

    /**
     * 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitPersonalScreenDerive(SysRoleSystemMenu systemMenu){
        return roleSystemMenuMapper.selectPermitPersonalScreenDerive(systemMenu);
    }

    /**
     * 仅获取个人衍生权限内模块&菜单
     * 访问控制 rsm 租户查询
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitPersonal(SysRoleSystemMenu systemMenu){
        return roleSystemMenuMapper.selectPermitPersonal(systemMenu);
    }
}
