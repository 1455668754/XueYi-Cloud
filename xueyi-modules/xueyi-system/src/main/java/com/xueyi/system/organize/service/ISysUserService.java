package com.xueyi.system.organize.service;

import java.util.List;

import com.xueyi.system.api.domain.organize.SysUser;

/**
 * 用户 业务层
 *
 * @author xueyi
 */
public interface ISysUserService {

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 登录日志用户检验
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    public SysUser checkUserByUserName(SysUser user);

    /**
     * 通过用户名查询用户
     *
     * @param user 用户信息 | userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(SysUser user);

    /**
     * 通过用户Id查询用户
     *
     * @param user 用户信息 | userId 用户Id
     * @return 用户对象信息
     */
    public SysUser selectUserById(SysUser user);

    /**
     * 根据用户Id查询用户所属角色组
     *
     * @param user 用户信息 | userName 用户名
     * @return 结果
     */
    public String selectUserRoleGroup(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改保存用户-角色信息
     *
     * @param user 用户信息 | userId  用户Id | roleIds 角色组Ids
     * @return 结果
     */
    public int updateUserRole(SysUser user);

    /**
     * 修改用户状态
     *
     * @param user 用户信息 | userId 用户Id | status 用户状态
     * @return 结果
     */
    public int updateUserStatus(SysUser user);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     *
     * @param user 用户信息 | userId 用户Id | avatar 头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(SysUser user);

    /**
     * 重置用户密码
     *
     * @param user 用户信息 | userId 用户Id | password 密码
     * @return 结果
     */
    public int resetUserPwd(SysUser user);

    /**
     * 通过用户Id删除用户
     *
     * @param user 用户信息 | userId 用户Id
     * @return 结果
     */
    public int deleteUserById(SysUser user);

    /**
     * 批量删除用户信息
     *
     * @param user 用户信息 | params.Ids 需要删除的用户Ids组
     * @return 结果
     */
    public int deleteUserByIds(SysUser user);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 校验用户编码是否唯一
     *
     * @param user 用户信息 | userId 用户Id | userCode 用户编码
     * @return 结果
     */
    public String checkUserCodeUnique(SysUser user);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息 | userId 用户Id | userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息 | userId 用户Id | phone 手机号码
     * @return 结果
     */
    public String checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息 | userId 用户Id | email email
     * @return 结果
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息 | userType 用户标识
     */
    public void checkUserAllowed(SysUser user);
}
