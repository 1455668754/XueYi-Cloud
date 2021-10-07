package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.authority.mapper.SysAuthorityMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.mapper.SysOrganizeRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    private SysAuthorityMapper authorityMapper;

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
        return roleMapper.selectRoleList(role);
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
    public int insertRole(SysRole role) {
        if (role.getType()!=null && !StringUtils.equals(role.getType(), AuthorityConstants.NORMAL_TYPE)) {
            SysOrganizeRole organizeRole = new SysOrganizeRole();
            switch (role.getType()){
                case AuthorityConstants.DERIVE_TENANT_TYPE:
                    organizeRole.setDeriveTenantId(role.getDeriveId());
                    break;
                case AuthorityConstants.DERIVE_ENTERPRISE_TYPE:
                    organizeRole.setDeriveEnterpriseId(role.getDeriveId());
                    break;
                case AuthorityConstants.DERIVE_DEPT_TYPE:
                    organizeRole.setDeriveDeptId(role.getDeriveId());
                    break;
                case AuthorityConstants.DERIVE_POST_TYPE:
                    organizeRole.setDerivePostId(role.getDeriveId());
                    break;
                case AuthorityConstants.DERIVE_USER_TYPE:
                    organizeRole.setDeriveUserId(role.getDeriveId());
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
        int rows = roleMapper.updateRoleDataScope(role);
        authorityMapper.deleteDeptPostByRoleId(role);
        if(role.getDeptPostIds().size()>0 && StringUtils.equals(AuthorityConstants.DATA_SCOPE_CUSTOM,role.getDataScope())){
            authorityMapper.insertDeptPostScope(role);
        }
        return (rows);
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
        int rows = roleMapper.deleteRoleByIds(role);
        if(rows>0){
            // 1.批量删除角色和系统-菜单关联
            authorityMapper.deleteSystemMenuByRoleIds(role);
            // 2.批量删除角色和部门-岗位关联
            authorityMapper.deleteDeptPostByRoleIds(role);
            // 3.批量删除组织和角色关联
            organizeRoleMapper.deleteOrganizeRoleByRoleIds(role);
        }
        return rows;
    }

    /**
     * 查询角色Id存在于数组中的角色信息
     *
     * @param role 角色信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    @Override
    public Set<SysRole> checkRoleListByIds(SysRole role){
        return roleMapper.checkRoleListByIds(role);
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

    /**
     * 通过类型和衍生Id查询角色Id与数据范围
     *
     * @param role       角色信息 | type 角色类型 | derive_id 衍生Id | enterpriseId 企业Id
     * @param sourceName 指定源
     * @return 角色Id
     */
    @Override
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
}