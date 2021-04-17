package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.role.mapper.SysDeptRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author xueyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysDeptRoleMapper deptRoleMapper;

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
        return deptMapper.selectDeptList(dept);//@param dept 部门信息
    }

    /**
     * 根据部门Id查询信息
     *
     * @param deptId 部门Id
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        return deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
    }

    /**
     * 根据Id查询所有子部门（正常状态）
     *
     * @param deptId 部门Id
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        return deptMapper.selectNormalChildrenDeptById(search);//@param search 万用组件 | deptId 部门Id
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysSearch search = new SysSearch();
        // 查询父节点是否正常状态,不正常则不允许新增子节点
        search.getSearch().put("deptId", dept.getParentId());
        SysDept info = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);//@param dept 部门信息
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
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", dept.getParentId());
        SysDept newParentDept = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
        search.getSearch().put("deptId", dept.getDeptId());
        SysDept oldDept = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        rows = deptMapper.updateDept(dept);//@param dept 部门信息
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        //执行部门状态变更
        updateDeptStatus(dept.getDeptId(), dept.getStatus());
        return rows;
    }

    /**
     * 修改保存部门-角色信息
     *
     * @param deptId  部门信息
     * @param roleIds 部门信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeptRole(Long deptId, Long[] roleIds) {
        //执行部门-角色变更 处理逻辑依次为：1.执行删除 → 2.是否需要执行新增
        SysSearch search = new SysSearch();
        //删除原有的deptRole信息
        search.getSearch().put("deptId", deptId);
        int rows = deptRoleMapper.deleteDeptRoleByDeptId(search);//@param search 万用组件 | deptId 部门Id
        if (roleIds.length > 0) {
            //改变为最新的deptRole信息
            search.getSearch().put("roleIds", roleIds);
            rows = rows + deptRoleMapper.batchDeptRole(search);//@param search 万用组件 | deptId 部门Id | roleIds 角色Ids(Long[])
        }
        return rows;
    }

    /**
     * 修改保存部门状态
     *
     * @param deptId 部门Id
     * @param status 部门状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeptStatus(Long deptId, String status) {
        int rows;
        SysSearch sear = new SysSearch();
        //停用时停用该部门的所有子部门以及其岗位和用户
        if (UserConstants.DEPT_DISABLE.equals(status)) {
            // 1.停用本部门所有岗位/用户的状态
            sear.getSearch().put("deptId", deptId);
            sear.getSearch().put("status", status);
            rows = postMapper.updatePostStatusByDeptId(sear);//@param search 万用组件 | deptId 部门Id | status 用户状态
            rows = rows + userMapper.updateUserStatusByDeptId(sear);//@param search 万用组件 | deptId 部门Id | status 用户状态
            // 2.获取所有子部门
            List<SysDept> deptList = deptMapper.selectChildrenDeptById(sear);//@param search 万用组件 | deptId 部门Id
            // 3.停用子部门所有岗位/用户的状态
            for (SysDept dept : deptList) {
                sear.getSearch().put("deptId", dept.getDeptId());
                sear.getSearch().put("status", status);
                rows = rows + postMapper.updatePostStatusByDeptId(sear);//@param search 万用组件 | deptId 部门Id | status 用户状态
                rows = rows + userMapper.updateUserStatusByDeptId(sear);//@param search 万用组件 | deptId 部门Id | status 用户状态
            }
        }
        // 3.变更部门状态
        sear.getSearch().put("deptId", deptId);
        sear.getSearch().put("status", status);
        return deptMapper.updateDeptStatus(sear);//@param search 万用组件 | deptId 部门Id | status 部门状态
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", dept.getDeptId());
        dept = deptMapper.selectDeptById(search);//@param search 万用组件 | deptId 部门Id
        deptMapper.updateAncestorsDeptStatus(dept);//@param dept 部门
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
        List<SysDept> children = deptMapper.selectChildrenDeptById(search);//@param search 万用组件 | deptId 部门Id
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            search.getSearch().put("depts", children);
            deptMapper.updateDeptChildren(search);//@param search 万用组件 | depts 子元素(List<SysDept>)
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteDeptById(Long deptId) {
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        rows = deptMapper.deleteDeptById(search);//@param search 万用组件 | deptId 部门Id
        if (rows > 0) {
            rows = rows + deptRoleMapper.deleteDeptRoleByDeptId(search);//@param search 万用组件 | deptId 部门Id
        }
        return rows;
    }

    /**
     * 校验是否存在子节点
     *
     * @param deptId 部门Id
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        int result = deptMapper.hasChildByDeptId(search);//@param search 万用组件 | deptId 部门Id
        return result > 0;
    }

    /**
     * 校验部门是否存在岗位
     *
     * @param deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistPost(Long deptId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        int result = postMapper.checkDeptExistPost(search);//@param search 万用组件 | deptId 部门Id
        return result > 0;
    }

    /**
     * 校验部门是否存在用户
     *
     * @param deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        int result = userMapper.checkDeptExistUser(search);//@param search 万用组件 | deptId 部门Id
        return result > 0;
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param deptId   部门Id
     * @param deptCode 部门编码
     * @return 结果
     */
    @Override
    public String checkDeptCodeUnique(Long deptId, String deptCode) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptCode", deptCode);
        SysDept info = deptMapper.checkDeptCodeUnique(search);//@param search 万用组件 | deptCode 部门编码
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param deptId   部门Id
     * @param parentId 父级Id
     * @param deptName 部门名称
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(Long deptId, Long parentId, String deptName) {
        if (StringUtils.isNull(deptId)) {
            deptId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("parentId", parentId);
        search.getSearch().put("deptName", deptName);
        SysDept info = deptMapper.checkDeptNameUnique(search);//@param search 万用组件 | parentId 父部门Id | deptName 部门名称
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验是否为父级的子级
     *
     * @param deptId   子级Id
     * @param parentId 父级Id
     * @return 结果
     */
    public String checkIsChild(Long deptId, Long parentId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("deptId", deptId);
        search.getSearch().put("parentId", parentId);
        SysDept info = deptMapper.checkIsChild(search);//@param search 万用组件 | deptId 子级Id | parentId 父级Id
        if (StringUtils.isNotNull(info)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
