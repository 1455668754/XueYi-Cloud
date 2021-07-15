package com.xueyi.system.organize.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.organize.SysUser;

/**
 * 用户表 数据层
 *
 * @author xueyi
 */
public interface SysUserMapper {

    /**
     * 通过租户Id&用户账号查询用户（登录校验）
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    public SysUser checkLoginByEnterpriseIdANDUserName(SysUser user);

    /**
     * 登录日志用户检验
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    public SysUser checkUserByUserName(SysUser user);

    /**
     * 根据条件分页查询用户列表
     * 访问控制 d 部门 | p 岗位 | u 用户 | u 租户查询
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", postAlias = "p", userAlias = "u", eAlias = "u")
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 通过用户账号查询用户
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | userName 用户名
     * @return 用户对象信息
     */
    @DataScope(eAlias = "u")
    public SysUser selectUserByUserName(SysUser user);

    /**
     * 通过用户Id查询用户
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | userId 用户Id
     * @return 用户对象信息
     */
    @DataScope(eAlias = "u")
    public SysUser selectUserById(SysUser user);

    /**
     * 新增用户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateUser(SysUser user);

    /**
     * 根据用户Id修改用户状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | userId 用户Id | status 用户状态
     */
    @DataScope(ueAlias = "empty")
    public int updateUserStatus(SysUser user);

    /**
     * 根据岗位Id修改用户状态（just禁用）
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | postId 岗位Id | status 用户状态
     */
    @DataScope(ueAlias = "empty")
    public int updateUserStatusByPostId(SysUser user);

    /**
     * 根据部门Id修改用户状态（just禁用）
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | deptId 部门Id | status 用户状态
     */
    @DataScope(ueAlias = "empty")
    public int updateUserStatusByDeptId(SysUser user);

    /**
     * 修改用户头像
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | userId 用户Id | avatar 头像地址
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateUserAvatar(SysUser user);

    /**
     * 重置用户密码
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | userId 用户Id | password 密码
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int resetUserPwd(SysUser user);

    /**
     * 通过用户Id删除用户
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | userId 用户Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteUserById(SysUser user);

    /**
     * 批量删除用户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息 | params.Ids 需要删除的用户Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteUserByIds(SysUser user);

    /**
     * 校验部门是否存在用户
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | deptId 部门Id
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public int checkDeptExistUser(SysUser user);

    /**
     * 校验岗位是否存在用户
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | postId 岗位Id
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public int checkPostExistUser(SysUser user);

    /**
     * 校验用户编码是否唯一
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | userCode 用户编码
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public SysUser checkUserCodeUnique(SysUser user);

    /**
     * 校验用户名称是否唯一
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | userName 用户名称
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public SysUser checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | phone 手机号码
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public SysUser checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     * 访问控制 u 租户查询
     *
     * @param user 用户信息 | email 用户邮箱
     * @return 结果
     */
    @DataScope(eAlias = "u")
    public SysUser checkEmailUnique(SysUser user);
}
