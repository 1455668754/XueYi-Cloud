package com.xueyi.system.authority.service;

import java.util.List;
import java.util.Set;

import com.xueyi.system.api.authority.SysRole;

/**
 * 角色业务层
 *
 * @author xueyi
 */
public interface ISysRoleService {

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */

    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户Id查询角色
     *
     * @param userId 用户Id
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 根据用户Id获取角色选择框列表
     *
     * @param userId 用户Id
     * @return 选中角色Id列表
     */
    public List<Integer> selectRoleListByUserId(Long userId);

    /**
     * 通过角色Id查询角色
     *
     * @param roleId 角色Id
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 通过角色Id查询角色使用数量
     *
     * @param roleId 角色Id
     * @return 结果
     */
    public int useCountByRoleId(Long roleId);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     *
     * @param roleId 角色Id
     * @param status 角色状态
     * @return 结果
     */
    public int updateRoleStatus(Long roleId, String status);

    /**
     * 修改菜单权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int authMenuScope(SysRole role);

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(SysRole role);

    /**
     * 通过角色Id删除角色
     *
     * @param roleId 角色Id
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * 校验角色编码是否唯一
     *
     * @param roleId   角色Id
     * @param roleCode 角色编码
     * @return 结果
     */
    public String checkRoleCodeUnique(Long roleId, String roleCode);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleId   角色Id
     * @param roleName 角色名称
     * @return 结果
     */
    public String checkRoleNameUnique(Long roleId, String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleId  角色Id
     * @param roleKey 角色权限
     * @return 结果
     */
    public String checkRoleKeyUnique(Long roleId, String roleKey);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);
}
