package com.xueyi.system.authority.service;

import java.util.List;

import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.role.domain.SysRoleDeptPost;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;

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
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 通过角色Id查询角色
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 角色对象信息
     */
    public SysRole selectRoleById(SysRole role);

    /**
     * 通过类型和衍生Id查询角色Id与数据范围
     *
     * @param role       角色信息 | type 角色类型 | derive_id 衍生Id | enterpriseId 企业Id
     * @param sourceName 指定源
     * @return 角色Id
     */
    public SysRole selectRoleIdByDeriveIdToSourceName(SysRole role, String sourceName);

    /**
     * 通过类型和衍生Id查询角色Id与数据范围
     *
     * @param role 角色信息 | type 角色类型 | derive_id 衍生Id | enterpriseId 企业Id
     * @return 角色Id
     */
    public SysRole selectRoleIdByDeriveId(SysRole role);

    /**
     * 根据角色Id获取菜单范围信息 - 获取尾级模块|菜单
     *
     * @return 结果
     */
    public List<SysMenu> selectSystemMenuListOnlyChild();

    /**
     * 根据角色Id获取数据范围信息
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 部门-岗位对象信息集合
     */
    public List<SysRoleDeptPost> selectDataScopeById(SysRole role);

    /**
     * 通过角色Id查询角色使用数量
     *
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    public int useCountByRoleId(SysRole role);

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
     * @param role 角色信息 | roleId 角色Id | status 角色状态
     * @return 结果
     */
    public int updateRoleStatus(SysRole role);

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
     * @param role 角色信息 | roleId 角色Id
     * @return 结果
     */
    public int deleteRoleById(SysRole role);

    /**
     * 批量删除角色信息
     *
     * @param role 角色信息 | params.Ids 需要删除的角色Ids组
     * @return 结果
     */
    public int deleteRoleByIds(SysRole role);

    /**
     * 校验角色编码是否唯一
     *
     * @param role 角色信息 | roleId   角色Id | roleCode 角色编码
     * @return 结果
     */
    public String checkRoleCodeUnique(SysRole role);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息 | roleId   角色Id | name 角色名称
     * @return 结果
     */
    public String checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息 | roleId  角色Id | roleKey 角色权限
     * @return 结果
     */
    public String checkRoleKeyUnique(SysRole role);
}
