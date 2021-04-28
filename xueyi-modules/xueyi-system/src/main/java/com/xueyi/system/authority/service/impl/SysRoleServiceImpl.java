package com.xueyi.system.authority.service.impl;

import java.util.*;

import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.role.mapper.*;
import com.xueyi.system.api.utilTool.SysSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.authority.service.ISysRoleService;

/**
 * 角色 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private SysRoleDeptPostMapper roleDeptPostMapper;

    @Autowired
    private SysDeptRoleMapper deptRoleMapper;

    @Autowired
    private SysPostRoleMapper postRoleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        SysSearch search = new SysSearch();
        return roleMapper.selectRoleAll(search);
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);//@param role 角色信息
    }

    /**
     * 通过角色Id查询角色
     *
     * @param roleId 角色Id
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", roleId);
        return roleMapper.selectRoleById(search);//@param search 万用组件 | roleId 角色Id
    }

//    /**
//     * 根据角色Id获取菜单范围信息
//     *
//     * @param roleId 角色Id
//     * @return 角色对象信息
//     */
//    @Override
//    public SysRole selectMenuScopeById(Long roleId){
//        return null;
//    }
//
//    /**
//     * 根据角色Id获取数据范围信息
//     *
//     * @param roleId 角色Id
//     * @return 角色对象信息
//     */
//    @Override
//    public SysRole selectDataScopeById(Long roleId){
//        SysSearch search = new SysSearch();
//        search.getSearch().put("roleId", roleId);
//        return null;
//    }

    /**
     * 通过角色Id查询角色使用数量
     *
     * @param roleId 角色Id
     * @return 结果
     */
    @Override
    public int useCountByRoleId(Long roleId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", roleId);
        int cr;
        // 1.通过角色Id查询部门-角色使用数量
        cr = deptRoleMapper.countDeptRoleByRoleId(search);//@param search 万用组件 | roleId 角色Id
        // 1.通过角色Id查询岗位-角色使用数量
        cr = cr + postRoleMapper.countPostRoleByRoleId(search);//@param search 万用组件 | roleId 角色Id
        // 1.通过角色Id查询用户-角色使用数量
        cr = cr + userRoleMapper.countUserRoleByRoleId(search);//@param search 万用组件 | roleId 角色Id
        return cr;
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int insertRole(SysRole role) {
        return roleMapper.insertRole(role);//@param role 角色信息
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRole(SysRole role) {
        return roleMapper.updateRole(role);//@param role 角色信息
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色Id
     * @param status 角色状态
     * @return 结果
     */
    @Override
    public int updateRoleStatus(Long roleId, String status) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", roleId);
        search.getSearch().put("status", status);
        return roleMapper.updateRoleStatus(search);//@param search 万用组件 | roleId 角色Id | status 角色状态
    }

    /**
     * 修改菜单权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authMenuScope(SysRole role) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", role.getRoleId());
        int rs;
        // 1.删除角色和系统-菜单关联
        rs = roleSystemMenuMapper.deleteRoleSystemMenuByRoleId(search);//@param search 查询组件 | roleId 角色Id
        // 2.批量新增角色和系统-菜单关联
        if (role.getSystemMenuIds().length > 0) {
            search.getSearch().put("systemMenuIds", role.getSystemMenuIds());
            rs = roleSystemMenuMapper.batchRoleSystemMenu(search);//@param search 万用组件 | roleId 角色Id | systemMenuIds 系统-菜单Ids(Long[])
        }
        return (rs + 1);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", role.getRoleId());
        search.getSearch().put("dataScope", role.getDataScope());
        int r;
        // 1.更新角色数据权限范围
        r = roleMapper.updateRoleDataScope(search);//@param search 万用组件 | roleId 角色Id | dataScope 数据范围
        // 2.删除角色和部门-岗位关联
        r = r + roleDeptPostMapper.deleteRoleDeptPostByRoleId(search);//@param search 查询组件 | roleId 角色Id

        // 3.批量新增角色和部门-岗位关联(仅设置为自定义数据时执行)
        if (role.getDeptPostIds().length > 0 && role.getDataScope().equals("2")) {
            search.getSearch().put("deptPostIds", role.getDeptPostIds());
            r = r + roleDeptPostMapper.batchRoleDeptPost(search);//@param search 万用组件 | roleId 角色Id | deptPostIds 部门Ids(Long[])
        }
        return (r);
    }


    /**
     * 通过角色Id删除角色
     *
     * @param roleId 角色Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        int r;
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", roleId);
        // 1.通过角色Id删除角色
        r = roleMapper.deleteRoleById(search);//@param search 万用组件 | roleId 角色Id
        if (r > 0) {
            // 1.删除角色和系统-菜单关联
            r = r + roleSystemMenuMapper.deleteRoleSystemMenuByRoleId(search);//@param search 查询组件 | roleId 角色Id
            // 2.删除角色和部门-岗位关联
            r = r + roleDeptPostMapper.deleteRoleDeptPostByRoleId(search);//@param search 查询组件 | roleId 角色Id
            // 3.删除部门和角色关联
            r = r + deptRoleMapper.deleteDeptRoleByRoleId(search);//@param search 查询组件 | roleId 角色Id
            // 4.删除岗位和角色关联
            r = r + postRoleMapper.deletePostRoleByRoleId(search);//@param search 查询组件 | roleId 角色Id
            // 5.删除用户和角色关联
            r = r + userRoleMapper.deleteUserRoleByRoleId(search);//@param search 查询组件 | roleId 角色Id
        }
        return r;
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        int rs;
        SysSearch search = new SysSearch();
        search.getSearch().put("roleIds", roleIds);
        // 1.批量删除角色信息
        rs = roleMapper.deleteRoleByIds(search);
        if (rs > 0) {
            // 1.批量删除角色和系统-菜单关联
            rs = rs + roleSystemMenuMapper.deleteRoleSystemMenuByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
            // 2.批量删除角色和部门-岗位关联
            rs = rs + roleDeptPostMapper.deleteRoleDeptPostByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
            // 3.批量删除部门和角色关联
            rs = rs + deptRoleMapper.deleteDeptRoleByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
            // 4.批量删除岗位和角色关联
            rs = rs + postRoleMapper.deletePostRoleByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
            // 5.批量删除用户和角色关联
            rs = rs + userRoleMapper.deleteUserRoleByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
        }
        return rs;
    }

    /**
     * 校验角色编码是否唯一
     *
     * @param roleId   角色Id
     * @param roleCode 角色编码
     * @return 结果
     */
    @Override
    public String checkRoleCodeUnique(Long roleId, String roleCode) {
        if (StringUtils.isNull(roleId)) {
            roleId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("roleCode", roleCode);
        SysRole info = roleMapper.checkRoleCodeUnique(search);//@param search 万用组件 | roleCode 角色编码
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param roleId   角色Id
     * @param roleName 角色名称
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(Long roleId, String roleName) {
        if (StringUtils.isNull(roleId)) {
            roleId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("roleName", roleName);
        SysRole info = roleMapper.checkRoleNameUnique(search);//@param search 万用组件 | roleName 角色名称
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param roleId  角色Id
     * @param roleKey 角色权限
     * @return 结果
     */
    public String checkRoleKeyUnique(Long roleId, String roleKey) {
        if (StringUtils.isNull(roleId)) {
            roleId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("roleKey", roleKey);
        SysRole info = roleMapper.checkRoleKeyUnique(search);//@param search 万用组件 | roleKey 角色权限
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }
}
