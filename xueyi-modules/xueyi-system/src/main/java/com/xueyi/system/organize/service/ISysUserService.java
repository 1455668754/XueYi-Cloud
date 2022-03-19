package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;

import java.io.Serializable;

/**
 * 用户管理 服务层
 *
 * @author xueyi
 */
public interface ISysUserService extends IBaseService<SysUserDto> {

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName 用户账号
     * @param password 密码
     * @return 用户对象
     */
    SysUserDto userLogin(String userName, String password);

    /**
     * 新增用户 | 内部调用
     *
     * @param user 用户对象
     * @return 结果
     */
    int addInner(SysUserDto user);

    /**
     * 修改用户基本信息
     *
     * @param id       用户Id
     * @param nickName 用户昵称
     * @param sex      用户性别
     * @param profile  个人简介
     * @return 结果
     */
    int updateUserProfile(Long id, String nickName, String sex, String profile);

    /**
     * 更新用户账号
     *
     * @param id       用户Id
     * @param userName 用户账号
     * @return 结果
     */
    int updateUserName(Long id, String userName);

    /**
     * 更新用户邮箱
     *
     * @param id    用户Id
     * @param email 邮箱
     * @return 结果
     */
    int updateEmail(Long id, String email);

    /**
     * 更新用户手机号
     *
     * @param id    用户Id
     * @param phone 手机号
     * @return 结果
     */
    int updatePhone(Long id, String phone);

    /**
     * 修改用户头像
     *
     * @param id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    int updateUserAvatar(Long id, String avatar);

    /**
     * 重置用户密码
     *
     * @param id       用户Id
     * @param password 密码
     * @return 结果
     */
    int resetUserPassword(Long id, String password);

    /**
     * 校验用户编码是否唯一
     *
     * @param id       用户Id
     * @param userCode 用户编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkUserCodeUnique(Long id, String userCode);

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkUserNameUnique(Serializable id, String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkPhoneUnique(Long id, String phone);

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkEmailUnique(Long id, String email);

    /**
     * 校验用户是否允许操作
     *
     * @param id 用户Id
     * @return 结果 | true/false 允许/禁止
     */
    boolean checkUserAllowed(Long id);

    /**
     * 用户数据脱敏
     *
     * @param user 用户对象
     */
    void userDesensitized(SysUserDto user);
}
