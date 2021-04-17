package com.xueyi.system.organize.service;

import java.util.List;

import com.xueyi.system.api.organize.SysDept;
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
     * 根据Id查询所有子部门（正常状态）
     *
     * @param deptId 部门Id
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

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
     * @param deptId 部门信息
     * @param roleIds 部门信息
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
}
