package com.xueyi.system.organize.service.impl;

import java.util.List;

import com.xueyi.system.api.utilTool.SysSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.organize.SysPost;
import com.xueyi.system.role.domain.SysUserRole;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.role.mapper.SysUserRoleMapper;
import com.xueyi.system.dict.service.ISysConfigService;
import com.xueyi.system.organize.service.ISysUserService;

/**
 * 用户 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);//@param sysUser 用户信息
    }

    /**
     * 通过用户账号查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userName", userName);
        return userMapper.selectUserByUserName(search);//@param search 万用组件 | userName 用户名
    }

    /**
     * 通过用户Id查询用户
     *
     * @param userId 用户Id
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        return userMapper.selectUserById(search);//@param search 万用组件 | userId 用户Id
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userName", userName);
        List<SysRole> list = roleMapper.selectRolesByUserName(search);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(updateEnterpriseAlias = "empty")
    public int insertUser(SysUser user) {
        int rows, t;
        SysSearch search = new SysSearch();
        // 1.更新用户状态 | t==0时代表归属岗位被禁用，则该用户也需变成禁用状态
        t = updateUserStatus(user.getUserId(), user.getPostId(), user.getStatus());
        if (t == 0) {
            user.setStatus(UserConstants.USER_DISABLE);
        }
        // 新增用户信息
        rows = userMapper.insertUser(user);
        // 新增用户-角色信息
        if (rows > 0 && user.getRoleIds().length > 0) {
            search.getSearch().put("roleIds", user.getRoleIds());
            rows = rows + userRoleMapper.batchNewUserRole(search); //@param search 万用组件 | 自动生成Id做userId | roleIds 角色Ids
        }
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        int rows, t;
        SysSearch search = new SysSearch();
        // 1.更新用户状态 | t==0时代表归属岗位被禁用，则该用户也需变成禁用状态
        t = updateUserStatus(user.getUserId(), user.getPostId(), user.getStatus());
        if (t == 0) {
            user.setStatus(UserConstants.USER_DISABLE);
        }
        // 修改用户信息
        rows = userMapper.updateUser(user);
        //执行用户-角色变更 处理逻辑依次为：1.判断是否变动 → 2.是否需要执行删除 → 3.是否需要执行新增
        if (rows > 0 && user.getRoleIds().length > 0) {
            search.getSearch().put("userId", user.getUserId());
            List<SysUserRole> userRoles = userRoleMapper.selectUserRoleByUserId(search);//@param search 万用组件 | userId 用户Id
            int k = 0;
            //1.检验roles是否变动
            if (userRoles.size() > 0) {
                for (SysUserRole userRole : userRoles) {
                    for (Long role : user.getRoleIds()) {
                        if (role.equals(userRole.getRoleId())) {
                            k++;
                        }
                    }
                }
            }
            if (k != userRoles.size() && k != user.getRoleIds().length) {
                //2.删除原有的postRole信息
                search.getSearch().put("userId", user.getUserId());
                search.getSearch().put("roleIds", user.getRoleIds());
                userRoleMapper.deleteUserRoleByUserId(search);//@param search 查询组件 | userId 用户Id
                //3.改变为最新的postRole信息
                rows = rows + userRoleMapper.batchUserRole(search);//@param search 万用组件 | userId 用户Id | roleIds 角色Ids
            }
        }
        return rows;
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户Id
     * @param postId 岗位Id
     * @param status 用户状态
     * @return 结果
     */
    @Override
    public int updateUserStatus(Long userId, Long postId, String status) {
        int rows = 1;
        SysSearch sear = new SysSearch();
        sear.getSearch().put("postId", postId);
        // 1.当岗位状态为禁用时，停用该用户
        if (UserConstants.USER_NORMAL.equals(status)) {
            SysPost post = postMapper.selectPostById(sear);//@param search 万用组件 | postId 岗位Id
            if (UserConstants.POST_DISABLE.equals(post.getStatus())) {
                status = UserConstants.USER_DISABLE;
                try {
                    rows = 0;
                    throw new CustomException(String.format("%1$s岗位已停用,无法启用该用户", post.getPostName()));
                } catch (Exception ignored) {
                }
            }
        }
        if (StringUtils.isNotNull(userId)) {
            sear.getSearch().put("userId", userId);
            sear.getSearch().put("status", status);
            rows = userMapper.updateUserStatus(sear);//@param search 万用组件 | userId 用户Id | status 用户状态
        }
        return rows;
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);//@param user 用户信息
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, String avatar) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        search.getSearch().put("avatar", avatar);
        return userMapper.updateUserAvatar(search) > 0;//@param search 万用组件 | userId 用户Id | avatar 头像地址
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户Id
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(Long userId, String password) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        search.getSearch().put("password", password);
        return userMapper.resetUserPwd(search);//@param search 万用组件 | userId 用户Id | password 密码
    }

    /**
     * 通过用户Id删除用户
     *
     * @param userId 用户Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        rows = userMapper.deleteUserById(search);//@param search 万用组件 | userId 用户Id
        if (rows > 0) {
            rows = rows + userRoleMapper.deleteUserRoleByUserId(search);//@param search 查询组件 | userId 用户Id
        }
        return rows;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 用户Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        int rows;
        SysSearch search = new SysSearch();
        search.getSearch().put("userIds", userIds);
        rows = userMapper.deleteUserByIds(search);//@param search 万用组件 | userIds 需要删除的用户Ids(Long[])
        if (rows > 0) {
            rows = rows + userRoleMapper.deleteUserRoleByIds(search);//@param search 查询组件 | userIds 需要删除的用户Ids(Long[])
        }
        return rows;
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        SysSearch search = new SysSearch();
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                search.getSearch().put("userName", user.getUserName());
                SysUser u = userMapper.selectUserByUserName(search);
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 校验用户编码是否唯一
     *
     * @param userId   用户Id
     * @param userCode 用户编码
     * @return 结果
     */
    @Override
    public String checkUserCodeUnique(Long userId, String userCode) {
        if (StringUtils.isNull(userId)) {
            userId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("userCode", userCode);
        SysUser info = userMapper.checkUserCodeUnique(search);//@param search 万用组件 | userCode 用户编码
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userId   用户Id
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(Long userId, String userName) {
        if (StringUtils.isNull(userId)) {
            userId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("userName", userName);
        SysUser info = userMapper.checkUserNameUnique(search);//@param search 万用组件 | userName 用户名称
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param userId 用户Id
     * @param phone  手机号码
     * @return 结果
     */
    @Override
    public String checkPhoneUnique(Long userId, String phone) {
        if (StringUtils.isNull(userId)) {
            userId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("phone", phone);
        SysUser info = userMapper.checkPhoneUnique(search);//@param search 万用组件 | phone 手机号码
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param userId 用户Id
     * @param email  email
     * @return 结果
     */
    @Override
    public String checkEmailUnique(Long userId, String email) {
        if (StringUtils.isNull(userId)) {
            userId = -1L;
        }
        SysSearch search = new SysSearch();
        search.getSearch().put("email", email);
        SysUser info = userMapper.checkEmailUnique(search);//@param search 万用组件 | email 用户邮箱
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param userType 用户标识
     */
    @Override
    public void checkUserAllowed(String userType) {
        if (SysUser.isAdmin(userType)) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }
}
