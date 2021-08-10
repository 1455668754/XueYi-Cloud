package com.xueyi.system.organize.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.RoleConstants;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.mapper.SysOrganizeRoleMapper;
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
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.dict.service.ISysConfigService;
import com.xueyi.system.organize.service.ISysUserService;

/**
 * 用户 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysOrganizeRoleMapper organizeRoleMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysPostMapper postMapper;

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
     * 登录日志用户检验
     *
     * @param user 用户信息 | enterpriseId 租户Id | userName 用户账号
     * @return 用户对象信息
     */
    public SysUser checkUserByUserName(SysUser user) {
        return userMapper.checkUserByUserName(user);
    }

    /**
     * 通过用户账号查询用户
     *
     * @param user 用户信息 | userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(SysUser user) {
        return userMapper.selectUserByUserName(user);
    }

    /**
     * 通过用户Id查询用户
     *
     * @param user 用户信息 | userId 用户Id
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(SysUser user) {
        return userMapper.selectUserById(user);
    }

    /**
     * 查询用户所属角色组
     *
     * @param user 用户信息 | userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(SysUser user) {
        SysRole checkRole = new SysRole();
        checkRole.getParams().put("userName", user.getUserName());
        List<SysRole> list = roleMapper.selectRolesByUserName(checkRole);
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
    @DataScope(ueAlias = "empty")
    public int insertUser(SysUser user) {
        // 欲启用用户时判断归属岗位是否启用，未启用则设置本用户为禁用状态
        SysPost post = new SysPost();
        post.setPostId(user.getPostId());
        SysPost info = postMapper.selectPostById(post);
        user.setDeptId(info.getDeptId());
        if (UserConstants.USER_NORMAL.equals(user.getStatus()) && UserConstants.POST_DISABLE.equals(info.getStatus())) {
            user.setStatus(UserConstants.USER_DISABLE);
            try {
                throw new CustomException(String.format("%1$s归属岗位已停用,无法启用该用户", user.getNickName()));
            } catch (Exception ignored) {
            }
        }
        int row = userMapper.insertUser(user);
        if(row>0){
            SysRole role = new SysRole();
            role.setType(RoleConstants.USER_DERIVE_TYPE);
            role.setDeriveId(user.getId());
            role.setRoleName("用户衍生"+user.getId());
            roleService.insertRole(role);
        }
        return row;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUser(SysUser user) {
        // 欲启用用户时判断归属岗位是否启用，未启用则设置本用户为禁用状态
        SysPost post = new SysPost();
        post.setPostId(user.getPostId());
        SysPost info = postMapper.selectPostById(post);
        user.setDeptId(info.getDeptId());
        if (UserConstants.USER_NORMAL.equals(user.getStatus()) && UserConstants.POST_DISABLE.equals(info.getStatus())) {
            user.setStatus(UserConstants.USER_DISABLE);
            try {
                throw new CustomException(String.format("%1$s归属岗位已停用,无法启用该用户", user.getNickName()));
            } catch (Exception ignored) {
            }
        }
        return userMapper.updateUser(user);//@param user 用户信息
    }

    /**
     * 修改保存用户-角色信息
     *
     * @param user 用户信息 | userId  用户Id | roleIds 角色组Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserRole(SysUser user) {
        // 1.删除原有用户-角色关联
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setUserId(user.getUserId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        // 2.是否需要执行新增
        if(user.getRoleIds().length > 0){
            organizeRole.getParams().put("roleIds",user.getRoleIds());
            organizeRoleMapper.batchOrganizeRole(organizeRole);
        }
        return 1;
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息 | userId 用户Id | status 用户状态
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUserStatus(user);
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
     * @param user 用户信息 | userId 用户Id | avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(SysUser user) {
        return userMapper.updateUserAvatar(user) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息 | userId 用户Id | password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user) {
        return userMapper.resetUserPwd(user);
    }

    /**
     * 通过用户Id删除用户
     *
     * @param user 用户信息 | userId 用户Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(SysUser user) {
        // 1.删除衍生role信息
        SysRole role = new SysRole();
        role.setType(RoleConstants.USER_DERIVE_TYPE);
        role.setDeriveId(user.getUserId());
        roleMapper.deleteRoleByTypeAndDeriveId(role);
        // 2.删除用户-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setUserId(user.getUserId());
        organizeRole.setDeriveUserId(user.getUserId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        return userMapper.deleteUserById(user);
    }

    /**
     * 批量删除用户信息
     *
     * @param user 用户信息 | params.Ids 需要删除的用户Ids组
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(SysUser user) {
        // 1.批量删除衍生role信息
        SysRole role = new SysRole();
        role.setType(RoleConstants.USER_DERIVE_TYPE);
        role.getParams().put("Ids",user.getParams().get("Ids"));
        roleMapper.deleteRoleByTypeAndDeriveIds(role);
        // 2.批量删除用户-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setUserId(RoleConstants.DELETE_PARAM);
        organizeRole.setDeriveUserId(RoleConstants.DELETE_PARAM);
        organizeRole.getParams().put("Ids",user.getParams().get("Ids"));
        organizeRoleMapper.deleteOrganizeRoleByOrganizeIds(organizeRole);
        return userMapper.deleteUserByIds(user);
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
                SysUser checkUser = new SysUser();
                checkUser.setUserName(user.getUserName());
                SysUser u = userMapper.selectUserByUserName(checkUser);
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
     * @param user 用户信息 | userId 用户Id | userCode 用户编码
     * @return 结果
     */
    @Override
    public String checkUserCodeUnique(SysUser user) {
        if (StringUtils.isNull(user.getUserId())) {
            user.setUserId(-1L);
        }
        SysUser info = userMapper.checkUserCodeUnique(user);
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != user.getUserId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息 | userId 用户Id | userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(SysUser user) {
        if (StringUtils.isNull(user.getUserId())) {
            user.setUserId(-1L);
        }
        SysUser info = userMapper.checkUserNameUnique(user);
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != user.getUserId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息 | userId 用户Id | phone 手机号码
     * @return 结果
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        if (StringUtils.isNull(user.getUserId())) {
            user.setUserId(-1L);
        }
        SysUser info = userMapper.checkPhoneUnique(user);
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != user.getUserId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息 | userId 用户Id | email email
     * @return 结果
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        if (StringUtils.isNull(user.getUserId())) {
            user.setUserId(-1L);
        }
        SysUser info = userMapper.checkEmailUnique(user);
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != user.getUserId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息 | userType 用户标识
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (SysUser.isAdmin(user.getUserType())) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }
}
