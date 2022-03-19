package com.xueyi.system.authority.service;

import com.xueyi.system.authority.domain.vo.SysAuthTree;

import java.util.List;

/**
 * 权限管理 服务层
 *
 * @author xueyi
 */
public interface ISysAuthService {

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectCommonAuthScope();

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectEnterpriseAuthScope();

    /**
     * 获取租户权限对象集合
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectTenantAuth();

    /**
     * 获取角色权限对象集合
     *
     * @param roleId 角色Id
     * @return 权限对象集合
     */
    List<SysAuthTree> selectRoleAuth(Long roleId);

    /**
     * 新增租户权限
     *
     * @param authIds 权限Ids
     */
    void addTenantAuth(Long[] authIds);

    /**
     * 修改租户权限
     *
     * @param authIds 权限Ids
     */
    void editTenantAuth(Long[] authIds);

    /**
     * 新增角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    void addRoleAuth(Long roleId, Long[] authIds);

    /**
     * 修改角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    void editRoleAuth(Long roleId, Long[] authIds);

}
