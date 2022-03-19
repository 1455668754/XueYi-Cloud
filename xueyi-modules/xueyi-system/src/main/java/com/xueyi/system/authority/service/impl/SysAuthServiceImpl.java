package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.manager.SysAuthManager;
import com.xueyi.system.authority.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysAuthServiceImpl implements ISysAuthService {

    @Autowired
    private SysAuthManager authManager;

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectCommonAuthScope() {
        return authManager.selectCommonAuthScope();
    }

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectEnterpriseAuthScope() {
        return authManager.selectEnterpriseAuthScope();
    }

    /**
     * 获取租户权限对象集合
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectTenantAuth() {
        return authManager.selectTenantAuth();
    }

    /**
     * 获取角色权限对象集合
     *
     * @param roleId 角色Id
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectRoleAuth(Long roleId) {
        return authManager.selectRoleAuth(roleId);
    }

    /**
     * 新增租户权限
     *
     * @param authIds 权限Ids
     */
    @Override
    @DSTransactional
    public void addTenantAuth(Long[] authIds) {
        authManager.addTenantAuth(authIds);
    }

    /**
     * 修改租户权限
     *
     * @param authIds 权限Ids
     */
    @Override
    @DSTransactional
    public void editTenantAuth(Long[] authIds) {
        authManager.editTenantAuth(authIds);
    }

    /**
     * 新增角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    @Override
    @DSTransactional
    public void addRoleAuth(Long roleId, Long[] authIds) {
        authManager.addRoleAuth(roleId, authIds);
    }

    /**
     * 修改角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    @Override
    @DSTransactional
    public void editRoleAuth(Long roleId, Long[] authIds) {
        authManager.editRoleAuth(roleId, authIds);
    }

}
