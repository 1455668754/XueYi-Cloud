package com.xueyi.auth.service;

import com.xueyi.auth.form.RegisterBody;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.tenant.api.RemoteTenantService;
import com.xueyi.tenant.api.model.TenantRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.enums.UserStatus;
import com.xueyi.common.core.exception.BaseException;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import com.xueyi.system.api.domain.monitor.SysLoginInfo;
import com.xueyi.system.api.RemoteLogService;
import com.xueyi.system.api.RemoteUserService;
import com.xueyi.system.api.domain.organize.SysUser;
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

    @Autowired
    private RemoteTenantService remoteTenantService;
    /**
     * 登录
     */
    public LoginUser login(String enterpriseName, String userName, String password) {
        // 企业账号||员工账号||密码为空 错误
        if (StringUtils.isAnyBlank(enterpriseName, userName, password)) {
            recordLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "企业账号/员工账号/密码必须填写");
            throw new BaseException("企业账号/员工账号/密码必须填写");
        }
        // 企业账号不在指定范围内 错误
        if (enterpriseName.length() < UserConstants.ENTERPRISENAME_MIN_LENGTH
                || enterpriseName.length() > UserConstants.ENTERPRISENAME_MAX_LENGTH) {
            recordLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "企业账号不在指定范围");
            throw new BaseException("企业账号不在指定范围");
        }
        // 员工账号不在指定范围内 错误
        if (userName.length() < UserConstants.USERNAME_MIN_LENGTH
                || userName.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "员工账号不在指定范围");
            throw new BaseException("员工账号不在指定范围");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            recordLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new BaseException("用户密码不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(enterpriseName, userName, SecurityConstants.INNER);
        if (R.FAIL == userResult.getCode()) {
            throw new BaseException(userResult.getMsg());
        }
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLoginInfo("master",-1L, enterpriseName, 0L, userName, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new BaseException("登录用户：" + userName + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysEnterprise enterprise = userInfo.getSysEnterprise();
        SysUser user = userInfo.getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new BaseException("对不起，您的账号：" + userName + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new BaseException("对不起，您的账号：" + userName + " 已停用");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_FAIL, "用户密码错误");
            throw new BaseException("用户不存在/密码错误");
        }
        recordLoginInfo(userInfo.getMainSource(), enterprise.getEnterpriseId(), enterpriseName, user.getUserId(), userName, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String sourceName, Long loginEnterpriseId, String loginEnterpriseName, Long loginUserId, String loginUserName) {
        recordLoginInfo(sourceName, loginEnterpriseId, loginEnterpriseName, loginUserId, loginUserName, Constants.LOGOUT, "退出成功");
    }


    /**
     * 注册
     */
    public void register(RegisterBody registerBody)
    {
        // 企业账号为空 错误
        if (StringUtils.isBlank(registerBody.getEnterpriseName()))
        {
            throw new BaseException("企业账号必须填写");
        }
        if (registerBody.getEnterpriseName().length() < UserConstants.ENTERPRISENAME_MIN_LENGTH
                || registerBody.getEnterpriseName().length() > UserConstants.ENTERPRISENAME_MAX_LENGTH)
        {
            throw new BaseException("企业账号长度必须在"+UserConstants.ENTERPRISENAME_MIN_LENGTH+"到"+UserConstants.ENTERPRISENAME_MAX_LENGTH+"个字符之间");
        }

        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(registerBody.getUserName(), registerBody.getPassword()))
        {
            throw new BaseException("用户账号/密码必须填写");
        }
        if (registerBody.getUserName().length() < UserConstants.USERNAME_MIN_LENGTH
                || registerBody.getUserName().length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new BaseException("用户账号长度必须在"+UserConstants.USERNAME_MIN_LENGTH+"到"+UserConstants.USERNAME_MAX_LENGTH+"个字符之间");
        }
        if (registerBody.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || registerBody.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new BaseException("用户密码长度必须在"+UserConstants.PASSWORD_MIN_LENGTH+"到"+UserConstants.PASSWORD_MAX_LENGTH+"个字符之间");
        }

        // 注册租户信息
        TenantRegister register = new TenantRegister();
        register.setEnterpriseName(registerBody.getEnterpriseName());
        register.setEnterpriseSystemName(registerBody.getEnterpriseSystemName());
        register.setEnterpriseNick(registerBody.getEnterpriseNick());
        register.setLogo(registerBody.getLogo());
        register.setUserName(registerBody.getUserName());
        register.setNickName(registerBody.getNickName());
        register.setEmail(registerBody.getEmail());
        register.setPhone(registerBody.getPhone());
        register.setSex(registerBody.getSex());
        register.setAvatar(registerBody.getAvatar());
        register.setProfile(registerBody.getProfile());
        register.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
        R<?> registerResult = remoteTenantService.registerTenantInfo(register, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new BaseException(registerResult.getMsg());
        }
        recordLoginInfo("master",-1L, register.getEnterpriseName(), 0L, register.getUserName(), Constants.REGISTER, "注册成功");
    }

    /**
     * 记录登录信息
     *
     * @param sourceName 索引数据源源
     * @param enterpriseId 企业Id
     * @param enterpriseName 企业名称
     * @param userId 用户Id
     * @param userName 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    public void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String status, String message)
    {
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setSourceName(sourceName);
        loginInfo.setEnterpriseId(enterpriseId);
        loginInfo.setEnterpriseName(enterpriseName);
        loginInfo.setUserId(userId);
        loginInfo.setUserName(userName);
        loginInfo.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        loginInfo.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            loginInfo.setStatus("0");
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            loginInfo.setStatus("1");
        }
        remoteLogService.saveLoginInfo(loginInfo, SecurityConstants.INNER);
    }
}