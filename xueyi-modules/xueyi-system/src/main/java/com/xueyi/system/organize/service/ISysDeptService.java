package com.xueyi.system.organize.service;

import java.util.List;

import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.utils.vo.TreeSelect;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysDeptService {

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据部门Id查询信息
     *
     * @param deptId 部门Id
     * @return 部门信息
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改保存部门-角色信息
     *
     * @param deptId  部门Id
     * @param roleIds 角色组Ids
     * @return 结果
     */
    public int updateDeptRole(Long deptId, Long[] roleIds);

    /**
     * 修改保存部门状态
     *
     * @param deptId 部门Id
     * @param status 部门状态
     * @return 结果
     */
    public int updateDeptStatus(Long deptId, String status);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 校验是否存在部门子节点
     *
     * @param deptId 部门Id
     * @return 结果
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * 校验部门是否存在岗位
     *
     * @param deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistPost(Long deptId);

    /**
     * 校验部门是否存在用户
     *
     * @param deptId 部门Id
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门编码是否唯一
     *
     * @param deptId   部门Id
     * @param deptCode 部门编码
     * @return 结果
     */
    public String checkDeptCodeUnique(Long deptId, String deptCode);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptId   部门Id
     * @param parentId 父级Id
     * @param deptName 部门名称
     * @return 结果
     */
    public String checkDeptNameUnique(Long deptId, Long parentId, String deptName);

    /**
     * 校验是否为父级的子级
     *
     * @param deptId   子级Id
     * @param parentId 父级Id
     * @return 结果
     */
    public String checkIsChild(Long deptId, Long parentId);

    /**
     * 校验已启用子部门数量(正常状态)
     *
     * @param deptId 部门Id
     * @return 子部门数
     */
    public int checkNormalChildrenCount(Long deptId);

    /**
     * 校验父级部门状态
     *
     * @param parentId 父级Id
     * @return 结果
     */
    public String checkParentDeptStatus(Long parentId);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);
}
