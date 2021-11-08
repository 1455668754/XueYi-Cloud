package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.TreeBuildUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.mapper.SysOrganizeRoleMapper;
import com.xueyi.system.utils.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysOrganizeRoleMapper organizeRoleMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 根据部门Id查询信息
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(SysDept dept) {
        return deptMapper.selectDeptById(dept);
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertDept(SysDept dept) {
        // 查询父节点是否正常状态,不正常则不允许新增子节点
        SysDept parentDept = new SysDept(dept.getParentId());
        SysDept info = deptMapper.selectDeptById(parentDept);
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        int row = deptMapper.insertDept(dept);
        if(row>0){
            SysRole role = new SysRole();
            role.setType(AuthorityConstants.DERIVE_DEPT_TYPE);
            role.setDeriveId(dept.getSnowflakeId());
            role.setName("部门衍生:"+dept.getSnowflakeId());
            roleService.insertRole(role);
        }
        return row;
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(new SysDept(dept.getParentId()));
        SysDept oldDept = deptMapper.selectDeptById(new SysDept(dept.getDeptId()));
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        // 欲启用部门时判断上级部门是否启用，未启用则设置本部门为禁用状态
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            if (UserConstants.DEPT_DISABLE.equals(checkDeptStatus(new SysDept(dept.getParentId())))) {
                dept.setStatus(UserConstants.DEPT_DISABLE);
                try {
                    throw new ServiceException(String.format("%1$s上级部门已停用,无法启用该部门", dept.getDeptName()));
                } catch (Exception ignored) {
                }
                updateDeptStatus(dept);
            }
        }
        // 执行部门状态变更
        return deptMapper.updateDept(dept);
    }

    /**
     * 修改保存部门-角色信息
     *
     * @param dept 部门信息 | deptId 部门Id | roleIds 角色组Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeptRole(SysDept dept) {
        // 1.删除原有部门-角色关联
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setDeptId(dept.getDeptId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        // 2.是否需要执行新增
        if(dept.getRoleIds().length > 0){
            organizeRole.getParams().put("roleIds",dept.getRoleIds());
            organizeRoleMapper.batchOrganizeRole(organizeRole);
        }
        return 1;
    }

    /**
     * 修改保存部门状态
     *
     * @param dept 部门信息 | deptId 部门Id | status 部门状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeptStatus(SysDept dept) {
        // 操作逻辑：当欲设置禁用时，同步执行禁用本部门所属岗位、用户
        int rows;
        // 变更部门状态
        rows = deptMapper.updateDeptStatus(dept);
        SysPost post = new SysPost();
        post.setDeptId(dept.getDeptId());
        post.setStatus(dept.getStatus());
        SysUser user = new SysUser();
        user.setDeptId(dept.getDeptId());
        user.setStatus(dept.getStatus());
        // 欲停用时停用本部门所有岗位/用户的状态
        if (rows > 0 && UserConstants.DEPT_DISABLE.equals(dept.getStatus())) {
            rows = rows + postMapper.updatePostStatusByDeptId(post);
            rows = rows + userMapper.updateUserStatusByDeptId(user);
        }
        return rows;
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门Id
     * @param newAncestors 新的父Id集合
     * @param oldAncestors 旧的父Id集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        SysDept dept = new SysDept(deptId);
        List<SysDept> children = deptMapper.selectChildrenDeptById(dept);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            dept.setDeptId(null);
            dept.getParams().put("depts", children);
            deptMapper.updateDeptChildren(dept);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteDeptById(SysDept dept) {
        // 1.删除衍生role信息
        SysRole role = new SysRole();
        role.setType(AuthorityConstants.DERIVE_DEPT_TYPE);
        role.setDeriveId(dept.getDeptId());
        roleMapper.deleteRoleByDeriveId(role);
        // 2.删除部门-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setDeptId(dept.getDeptId());
        organizeRole.setDeriveDeptId(dept.getDeptId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        return deptMapper.deleteDeptById(dept);
    }

    /**
     * 校验是否存在子节点
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(SysDept dept) {
        int result = deptMapper.hasChildByDeptId(dept);
        return result > 0;
    }

    /**
     * 校验部门是否存在岗位
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistPost(SysDept dept) {
        SysPost post = new SysPost();
        post.setDeptId(dept.getDeptId());
        int result = postMapper.checkDeptExistPost(post);
        return result > 0;
    }

    /**
     * 校验部门是否存在用户
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(SysDept dept) {
        SysUser user = new SysUser();
        user.setDeptId(dept.getDeptId());
        int result = userMapper.checkDeptExistUser(user);
        return result > 0;
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param dept 部门信息 | deptId 部门Id | deptCode 部门编码
     * @return 结果
     */
    @Override
    public String checkDeptCodeUnique(SysDept dept) {
        if (StringUtils.isNull(dept.getDeptId())) {
            dept.setDeptId(-1L);
        }
        SysDept info = deptMapper.checkDeptCodeUnique(dept);
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != dept.getDeptId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息 | deptId 部门Id | parentId 父级Id | deptName 部门名称
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        if (StringUtils.isNull(dept.getDeptId())) {
            dept.setDeptId(-1L);
        }
        SysDept info = deptMapper.checkDeptNameUnique(dept);
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != dept.getDeptId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验是否为父级的子级
     *
     * @param dept 部门信息 | deptId 子级Id | parentId 父级Id
     * @return 结果
     */
    @Override
    public String checkIsChild(SysDept dept) {
        SysDept info = deptMapper.checkIsChild(dept);
        if (StringUtils.isNotNull(info)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验已启用子部门数量(正常状态)
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 子部门数
     */
    @Override
    public int checkNormalChildrenCount(SysDept dept) {
        return deptMapper.checkNormalChildrenCount(dept);
    }

    /**
     * 校验部门状态
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果
     */
    @Override
    public String checkDeptStatus(SysDept dept) {
        if (StringUtils.isNotNull(dept.getDeptId()) && dept.getDeptId() != 0L) {
            SysDept info = deptMapper.selectDeptById(dept);
            if (StringUtils.isNotNull(info) && UserConstants.DEPT_DISABLE.equals(info.getStatus())) {
                return UserConstants.DEPT_DISABLE;
            }
        }
        return UserConstants.DEPT_NORMAL;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = TreeBuildUtils.buildSystemMenuTree(depts,"deptId", "parentId", "children", null, false);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}