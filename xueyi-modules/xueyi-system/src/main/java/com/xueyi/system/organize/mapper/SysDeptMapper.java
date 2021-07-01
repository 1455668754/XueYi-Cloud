package com.xueyi.system.organize.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.organize.SysDept;

/**
 * 部门管理 数据层
 *
 * @author xueyi
 */
public interface SysDeptMapper {
    /**
     * 查询部门管理数据
     * 访问控制 d 部门 | d 租户查询
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @DataScope(deptAlias = "d", eAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据部门Id查询信息
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 部门信息
     */
    @DataScope(eAlias = "d")
    public SysDept selectDeptById(SysDept dept);

    /**
     * 根据Id查询所有子部门
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 部门列表
     */
    @DataScope(eAlias = "d")
    public List<SysDept> selectChildrenDeptById(SysDept dept);

    /**
     * 新增部门信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param dept 部门信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param dept 部门信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateDept(SysDept dept);

    /**
     * 修改保存部门状态
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 部门Id | status 部门状态
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param dept 部门信息 | params.depts 子元素(List<SysDept>)
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateDeptChildren(SysDept dept);

    /**
     * 删除部门管理信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteDeptById(SysDept dept);

    /**
     * 校验是否存在子节点
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 部门Id
     * @return 结果
     */
    @DataScope(eAlias = "d")
    public int hasChildByDeptId(SysDept dept);

    /**
     * 校验部门编码是否唯一
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptCode 部门编码
     * @return 结果
     */
    @DataScope(eAlias = "d")
    public SysDept checkDeptCodeUnique(SysDept dept);

    /**
     * 校验部门名称是否唯一
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | parentId 父部门Id | deptName 部门名称
     * @return 结果
     */
    @DataScope(eAlias = "d")
    public SysDept checkDeptNameUnique(SysDept dept);

    /**
     * 校验是否为父级的子级
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 子级Id | parentId 父级Id
     * @return 结果
     */
    @DataScope(eAlias = "d")
    public SysDept checkIsChild(SysDept dept);

    /**
     * 校验已启用子部门数量(正常状态)
     * 访问控制 d 租户查询
     *
     * @param dept 部门信息 | deptId 子级Id
     * @return 子部门数
     */
    @DataScope(eAlias = "d")
    public int checkNormalChildrenCount(SysDept dept);
}
