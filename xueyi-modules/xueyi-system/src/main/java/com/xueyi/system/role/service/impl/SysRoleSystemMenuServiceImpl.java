package com.xueyi.system.role.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.RoleConstants;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.authority.mapper.SysRoleMapper;
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
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    /**
     * 获取指定企业账号的租管衍生角色菜单范围信息 | 租管系统使用方法
     *
     * @param systemMenu 角色和系统关联对象 | sourceName 指定源 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    @DS("#systemMenu.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SysRoleSystemMenu> getEnterpriseMenuScopeById(SysRoleSystemMenu systemMenu){
        SysRole role = new SysRole();
        role.setType(RoleConstants.ADMINISTRATOR_DERIVE_TYPE);
        role.setDeriveId(systemMenu.getEnterpriseId());
        role.setEnterpriseId(systemMenu.getEnterpriseId());
        systemMenu.setRoleId(roleMapper.selectDeriveRoleByEnterpriseId(role));
        return roleSystemMenuMapper.getEnterpriseMenuScopeByEnterpriseId(systemMenu);
    }

    /**
     * 修改保存指定企业账号的租管衍生角色菜单权限 | 租管系统使用方法
     *
     * @param systemMenu 角色和系统关联对象 | sourceName 指定源 | params.systemMenuIds 模块&菜单Id集合 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    @DS("#systemMenu.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int authMenuScopeById(SysRoleSystemMenu systemMenu){
        SysRole role = new SysRole();
        role.setType(RoleConstants.ADMINISTRATOR_DERIVE_TYPE);
        role.setDeriveId(systemMenu.getEnterpriseId());
        role.setEnterpriseId(systemMenu.getEnterpriseId());
        systemMenu.setRoleId(roleMapper.selectDeriveRoleByEnterpriseId(role));
        roleSystemMenuMapper.deleteMenuScopeByEnterpriseId(systemMenu);
        List<Long> Ids = (List<Long>) systemMenu.getParams().get("systemMenuIds");
        if(Ids.size()>0){
            roleSystemMenuMapper.authMenuScopeByEnterpriseId(systemMenu);
        }
        return 1;
    }

    /**
     * 仅获取超管衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitAdministrator(SysRoleSystemMenu systemMenu) {
        return roleSystemMenuMapper.selectPermitAdministrator(systemMenu);
    }

    /**
     * 仅获取租户衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitEnterprise(SysRoleSystemMenu systemMenu) {
        return roleSystemMenuMapper.selectPermitEnterprise(systemMenu);
    }

    /**
     * 仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitPersonalScreenDerive(SysRoleSystemMenu systemMenu) {
        return roleSystemMenuMapper.selectPermitPersonalScreenDerive(systemMenu);
    }

    /**
     * 仅获取个人衍生权限内模块&菜单
     *
     * @param systemMenu 角色和系统关联对象 | enterpriseId 租户Id | params.userId 用户Id
     * @return 结果
     */
    @Override
    public List<SysRoleSystemMenu> selectPermitPersonal(SysRoleSystemMenu systemMenu) {
        return roleSystemMenuMapper.selectPermitPersonal(systemMenu);
    }
}
