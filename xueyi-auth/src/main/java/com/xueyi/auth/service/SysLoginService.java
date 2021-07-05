package com.xueyi.auth.service;

import com.xueyi.system.api.organize.SysEnterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.enums.UserStatus;
import com.xueyi.common.core.exception.BaseException;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.RemoteLogService;
import com.xueyi.system.api.RemoteUserService;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.model.LoginUser;

/**
 * 登录校验方法
 *
 * @author xueyi
 */
@Component
public class SysLoginService {
    @Autowired
    private RemoteLogService remoteLogService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 登录
     */
    public LoginUser login(String enterpriseName, String userName, String password) {
        // 企业账号||员工账号||密码为空 错误
        if (StringUtils.isAnyBlank(enterpriseName, userName, password)) {
            remoteLogService.saveLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "企业账号/员工账号/密码必须填写");
            throw new BaseException("企业账号/员工账号/密码必须填写");
        }
        // 企业账号不在指定范围内 错误
        if (enterpriseName.length() < UserConstants.ENTERPRISENAME_MIN_LENGTH
                || enterpriseName.length() > UserConstants.ENTERPRISENAME_MAX_LENGTH) {
            remoteLogService.saveLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "企业账号不在指定范围");
            throw new BaseException("企业账号不在指定范围");
        }
        // 员工账号不在指定范围内 错误
        if (userName.length() < UserConstants.USERNAME_MIN_LENGTH
                || userName.length() > UserConstants.USERNAME_MAX_LENGTH) {
            remoteLogService.saveLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "员工账号不在指定范围");
            throw new BaseException("员工账号不在指定范围");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            remoteLogService.saveLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new BaseException("用户密码不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(enterpriseName, userName);
        if (R.FAIL == userResult.getCode()) {
            throw new BaseException(userResult.getMsg());
        }
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            remoteLogService.saveLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new BaseException("登录用户：" + userName + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysEnterprise enterprise = userInfo.getSysEnterprise();
        SysUser user = userInfo.getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            remoteLogService.saveLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new BaseException("对不起，您的账号：" + userName + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            remoteLogService.saveLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new BaseException("对不起，您的账号：" + userName + " 已停用");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            remoteLogService.saveLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "用户密码错误");
            throw new BaseException("用户不存在/密码错误");
        }
        remoteLogService.saveLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String SourceName, Long loginEnterpriseId, String loginEnterpriseName, Long loginUserId, String loginUserName) {
        remoteLogService.saveLoginInfo(SourceName, loginEnterpriseId, loginEnterpriseName, loginUserId, loginUserName, Constants.LOGOUT, "退出成功");
    }
}