package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.role.domain.SysDeptRole;
import com.xueyi.system.api.utilTool.SysSearch;

import java.util.List;

/**
 * 部门和角色关联Mapper接口
 *
 * @author xueyi
 */
public interface SysDeptRoleMapper {

    /**
     * 通过部门Id查询列表
     * 访问控制 dr 租户查询
     *
     * @param search 万用组件 | deptId 部门Id
     * @return 结果
     */
    @DataScope(eAlias = "dr")
    public List<SysDeptRole> selectDeptRoleByDeptId(SysSearch search);

    /**
     * 通过角色Id查询部门-角色使用数量
     * 访问控制 dr 租户查询
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(eAlias = "dr")
    public int countDeptRoleByRoleId(SysSearch search);

    /**
     * 批量新增部门和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | deptId 部门Id | roleIds 角色Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int batchDeptRole(SysSearch search);

    /**
     * 删除部门和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | roleId 角色Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteDeptRoleByRoleId(SysSearch search);

    /**
     * 批量删除部门和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 查询组件 | roleIds 需要删除的角色Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteDeptRoleByRoleIds(SysSearch search);

    /**
     * 删除部门和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | deptId 部门Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteDeptRoleByDeptId(SysSearch search);

    /**
     * 批量删除部门和角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | deptIds 需要删除的部门Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteDeptRoleByIds(SysSearch search);
}
