package com.xueyi.system.organize.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import org.apache.ibatis.annotations.Param;

import com.xueyi.system.api.organize.SysUser;

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
     * @param search 万用组件 | enterpriseId 租户Id | userName 用户账号
     * @param
     * @return 用户对象信息
     */
    public SysUser checkLoginByEnterpriseIdANDUserName(SysSearch search);

    /**
     * 根据条件分页查询用户列表
     * 访问控制 u 部门 | u 租户查询
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "u", enterpriseAlias = "u")
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通过用户账号查询用户
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | userName 用户名
     * @return 用户对象信息
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser selectUserByUserName(SysSearch search);

    /**
     * 通过用户Id查询用户
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | userId 用户Id
     * @return 用户对象信息
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser selectUserById(SysSearch search);

    /**
     * 新增用户信息
     * 访问控制 empty 租户更新（无前缀）(控制器在SysUserServiceImpl层 insertUser方法)
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param user 用户信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateUser(SysUser user);

    /**
     * 根据用户Id修改用户状态
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userId 用户Id | status 用户状态
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateUserStatus(SysSearch search);

    /**
     * 根据岗位Id修改用户状态（just禁用）
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | postId 岗位Id | status 用户状态
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateUserStatusByPostId(SysSearch search);

    /**
     * 根据部门Id修改用户状态（just禁用）
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | deptId 部门Id | status 用户状态
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateUserStatusByDeptId(SysSearch search);

    /**
     * 修改用户头像
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userId 用户Id | avatar 头像地址
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateUserAvatar(SysSearch search);

    /**
     * 重置用户密码
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userId 用户Id | password 密码
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int resetUserPwd(SysSearch search);

    /**
     * 通过用户Id删除用户
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userId 用户Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteUserById(SysSearch search);

    /**
     * 批量删除用户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | userIds 需要删除的用户Ids(Long[])
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteUserByIds(SysSearch search);

    /**
     * 校验部门是否存在用户
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | deptId 部门Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public int checkDeptExistUser(SysSearch search);

    /**
     * 校验岗位是否存在用户
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | postId 岗位Id
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public int checkPostExistUser(SysSearch search);

    /**
     * 校验用户编码是否唯一
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | userCode 用户编码
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser checkUserCodeUnique(SysSearch search);

    /**
     * 校验用户名称是否唯一
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | userName 用户名称
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser checkUserNameUnique(SysSearch search);

    /**
     * 校验手机号码是否唯一
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | phone 手机号码
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser checkPhoneUnique(SysSearch search);

    /**
     * 校验email是否唯一
     * 访问控制 u 租户查询
     *
     * @param search 万用组件 | email 用户邮箱
     * @return 结果
     */
    @DataScope(enterpriseAlias = "u")
    public SysUser checkEmailUnique(SysSearch search);
}
