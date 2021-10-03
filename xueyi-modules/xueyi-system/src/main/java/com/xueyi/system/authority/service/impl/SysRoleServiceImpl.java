package com.xueyi.system.authority.service.impl;

import java.util.*;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.RoleConstants;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.domain.SysRoleDeptPost;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.role.mapper.*;
import com.xueyi.system.api.utilTool.SysSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.authority.service.ISysRoleService;

/**
 * 角色 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private SysRoleDeptPostMapper roleDeptPostMapper;

    @Autowired
    private SysOrganizeRoleMapper organizeRoleMapper;

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return roleMapper.selectRoleAll(new SysRole());
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
     * @param role 角色信息 | roleId 角色Id
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(SysRole role) {
        return roleMapper.selectRoleById(role);
    }

    /**
     * 通过类型和衍生Id查询角色Id与数据范围
     *
     * @param role       角色信息 | type 角色类型 | derive_id 衍生Id | enterpriseId 企业Id
     * @param sourceName 指定源
     * @return 角色Id
     */
    @DS("#sourceName")
    public SysRole selectRoleIdByDeriveIdToSourceName(SysRole role, String sourceName) {
        return roleMapper.selectRoleIdByDeriveId(role);
    }

    /**
     * 通过类型和衍生Id查询角色Id与数据范围
     *
     * @param role 角色信息 | type 角色类型 | derive_id 衍生Id | enterpriseId 企业Id
     * @return 角色Id
     */
    @Override
    public SysRole selectRoleIdByDeriveId(SysRole role) {
        return roleMapper.selectRoleIdByDeriveId(role);
    }

    /**
     * 根据角色Id获取菜单范围信息 - 获取尾级模块|菜单
     *
     * @return 结果
     */
    @Override
    @DS("#main")
    public List<SysMenu> selectSystemMenuListOnlyChild() {
        return menuMapper.selectSystemMenuListOnlyChild(new SysMenu());
    }

    /**
     * 根据角色Id获取数据范围信息
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 部门-岗位对象信息集合
     */
    @Override
    public List<SysRoleDeptPost> selectDataScopeById(SysRole role) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", role.getRoleId());
        return roleDeptPostMapper.selectDeptPostList(search);//@param search 万用组件 | roleId 角色Id
    }

    /**
     * 通过角色Id查询角色使用数量
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    @Override
    public int useCountByRoleId(SysRole role) {
        return organizeRoleMapper.countOrganizeRoleByRoleId(role);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertRole(SysRole role) {
        if (!StringUtils.equals(role.getType(), RoleConstants.ROUTINE_DERIVE_TYPE)) {
            SysOrganizeRole organizeRole = new SysOrganizeRole();
            organizeRole.setRoleId(role.getSnowflakeId());
            if (StringUtils.equals(role.getType(), RoleConstants.DEPT_DERIVE_TYPE)) {
                organizeRole.setDeriveDeptId(role.getDeriveId());
            } else if (StringUtils.equals(role.getType(), RoleConstants.POST_DERIVE_TYPE)) {
                organizeRole.setDerivePostId(role.getDeriveId());
            } else if (StringUtils.equals(role.getType(), RoleConstants.USER_DERIVE_TYPE)) {
                organizeRole.setDeriveUserId(role.getDeriveId());
            } else if (StringUtils.equals(role.getType(), RoleConstants.ENTERPRISE_DERIVE_TYPE)) {
                organizeRole.setDeriveEnterpriseId(role.getDeriveId());
            }
            organizeRoleMapper.insertOrganizeRole(organizeRole);
        }
        return roleMapper.insertRole(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRole(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息 | roleId 角色Id | status 角色状态
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRoleStatus(role);
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
        int r;
        // 1.更新角色数据权限范围
        r = roleMapper.updateRoleDataScope(role);
        // 2.删除角色和部门-岗位关联
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", role.getRoleId());
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
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(SysRole role) {
        int r;
        SysSearch search = new SysSearch();
        search.getSearch().put("roleId", role.getRoleId());
        // 1.通过角色Id删除角色
        // 1.删除角色和系统-菜单关联
        roleSystemMenuMapper.deleteRoleSystemMenuByRoleId(search);//@param search 查询组件 | roleId 角色Id
        // 2.删除角色和部门-岗位关联
        roleDeptPostMapper.deleteRoleDeptPostByRoleId(search);//@param search 查询组件 | roleId 角色Id
        // 3.删除组织和角色关联
        organizeRoleMapper.deleteOrganizeRoleByRoleId(role);//role 角色信息 | roleId 角色Id
        return roleMapper.deleteRoleById(role);
    }

    /**
     * 批量删除角色信息
     *
     * @param role 角色信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(SysRole role) {
        SysSearch search = new SysSearch();
        search.getSearch().put("roleIds", role.getParams().get("Ids"));
        // 1.批量删除角色信息
        // 1.批量删除角色和系统-菜单关联
        roleSystemMenuMapper.deleteRoleSystemMenuByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
        // 2.批量删除角色和部门-岗位关联
        roleDeptPostMapper.deleteRoleDeptPostByIds(search);//@param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
        // 3.批量删除组织和角色关联
        organizeRoleMapper.deleteOrganizeRoleByRoleIds(role);//role 角色信息 | params.Ids 需要删除的角色Ids组
        return roleMapper.deleteRoleByIds(role);
    }

    /**
     * 校验角色编码是否唯一
     *
     * @param role 角色信息 | roleId   角色Id | roleCode 角色编码
     * @return 结果
     */
    @Override
    public String checkRoleCodeUnique(SysRole role) {
        if (StringUtils.isNull(role.getRoleId())) {
            role.setRoleId(-1L);
        }
        SysRole info = roleMapper.checkRoleCodeUnique(role);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != role.getRoleId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息 | roleId   角色Id | name 角色名称
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        if (StringUtils.isNull(role.getRoleId())) {
            role.setRoleId(-1L);
        }
        SysRole info = roleMapper.checkRoleNameUnique(role);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != role.getRoleId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息 | roleId  角色Id | roleKey 角色权限
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        if (StringUtils.isNull(role.getRoleId())) {
            role.setRoleId(-1L);
        }
        SysRole info = roleMapper.checkRoleKeyUnique(role);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != role.getRoleId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}