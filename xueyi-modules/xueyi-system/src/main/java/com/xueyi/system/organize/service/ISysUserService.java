package com.xueyi.system.organize.service;

import java.util.List;

import com.xueyi.system.api.organize.SysUser;

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
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户Id查询用户
     *
     * @param userId 用户Id
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 根据用户Id查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    public String selectUserRoleGroup(String userName);

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
     * @param userId  用户Id
     * @param roleIds 角色组Ids
     * @return 结果
     */
    public int updateUserRole(Long userId, Long[] roleIds);

    /**
     * 修改用户状态
     *
     * @param userId 用户Id
     * @param status 用户状态
     * @return 结果
     */
    public int updateUserStatus(Long userId, String status);

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
     * @param userId 用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(Long userId, String avatar);

    /**
     * 重置用户密码
     *
     * @param userId   用户Id
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(Long userId, String password);

    /**
     * 通过用户Id删除用户
     *
     * @param userId 用户Id
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

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
     * @param userId   用户Id
     * @param userCode 用户编码
     * @return 结果
     */
    public String checkUserCodeUnique(Long userId, String userCode);

    /**
     * 校验用户名称是否唯一
     *
     * @param userId   用户Id
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(Long userId, String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param userId 用户Id
     * @param phone  手机号码
     * @return 结果
     */
    public String checkPhoneUnique(Long userId, String phone);

    /**
     * 校验email是否唯一
     *
     * @param userId 用户Id
     * @param email  email
     * @return 结果
     */
    public String checkEmailUnique(Long userId, String email);

    /**
     * 校验用户是否允许操作
     *
     * @param userType 用户标识
     */
    public void checkUserAllowed(String userType);
}
