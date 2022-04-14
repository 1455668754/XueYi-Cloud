package com.xueyi.auth.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.auth.form.RegisterBody;
import com.xueyi.common.core.constant.basic.*;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import com.xueyi.system.api.authority.feign.RemoteLoginService;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.feign.RemoteLogService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.tenant.api.tenant.feign.RemoteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private RemoteLoginService remoteLoginService;

    @Autowired
    private RemoteTenantService remoteTenantService;

    /**
     * 登录
     */
    public LoginUser login(String enterpriseName, String userName, String password) {
        // 企业账号||员工账号||密码为空 错误
        if (StrUtil.hasBlank(enterpriseName, userName, password)) {
            recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号/员工账号/密码必须填写");
            throw new ServiceException("企业账号/员工账号/密码必须填写");
        }
        // 企业账号不在指定范围内 错误
        if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH
                || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH) {
            recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "企业账号不在指定范围");
            throw new ServiceException("企业账号不在指定范围");
        }

        // 员工账号不在指定范围内 错误
        if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH
                || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH) {
            recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "员工账号不在指定范围");
            throw new ServiceException("员工账号不在指定范围");
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH
                || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH) {
            recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 查询登录信息
        R<LoginUser> loginInfoResult = remoteLoginService.getLoginInfoInner(enterpriseName, userName, password, SecurityConstants.INNER);
        if (loginInfoResult.isFail()) {
            throw new ServiceException("当前访问人数过多，请稍后再试！");
        } else if (ObjectUtil.isNull(loginInfoResult.getResult())) {
            recordLoginInfo(enterpriseName, userName, Constants.LOGIN_FAIL, loginInfoResult.getMessage());
            throw new ServiceException("企业账号/员工账号/密码错误，请检查！");
        }
        LoginUser loginUser = loginInfoResult.getResult();
        Long enterpriseId = loginUser.getEnterpriseId();
        String sourceName = loginUser.getSourceName();
        SysUserDto user = loginUser.getUser();
        Long userId = user.getId();
        String userNick = user.getNickName();
        if (BaseConstants.Status.DISABLE.getCode().equals(loginUser.getUser().getStatus())) {
            recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + userName + " 已停用！");
        }

        recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGIN_SUCCESS, "登录成功");
        return loginUser;
    }

    /**
     * 退出
     */
    public void logout(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String userNick) {
        recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGOUT, "退出成功");
    }


    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        // 注册租户信息
        R<?> registerResult = remoteTenantService.registerTenantInfo(registerBody.buildJson(), SecurityConstants.INNER);
        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMessage());
        }
        // 注册逻辑补充完整后再增加日志
//        recordLoginInfo(TenantConstants.Source.SLAVE.getCode(), SecurityConstants.EMPTY_TENANT_ID, registerBody.getTenant().getName(), SecurityConstants.EMPTY_USER_ID, registerBody.getUser().getUserName(), Constants.REGISTER, "注册成功");
    }

    /**
     * 记录登录信息 | 无企业信息
     *
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    public void recordLoginInfo(String enterpriseName, String userName, String status, String message) {
        recordLoginInfo(TenantConstants.Source.SLAVE.getCode(), SecurityConstants.EMPTY_TENANT_ID, enterpriseName, SecurityConstants.EMPTY_USER_ID, userName, StrUtil.EMPTY, status, message);
    }

    /**
     * 记录登录信息 | 无用户信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    public void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, String userName, String status, String message) {
        recordLoginInfo(sourceName, enterpriseId, enterpriseName, SecurityConstants.EMPTY_USER_ID, userName, StrUtil.EMPTY, status, message);
    }

    /**
     * 记录登录信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userId         用户Id
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    public void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String userNick, String status, String message) {
        SysLoginLogDto loginInfo = new SysLoginLogDto();
        loginInfo.setEnterpriseId(enterpriseId);
        loginInfo.setEnterpriseName(enterpriseName);
        loginInfo.setUserId(userId);
        loginInfo.setUserName(userName);
        loginInfo.setUserNick(userNick);
        loginInfo.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        loginInfo.setMsg(message);
        // 日志状态
        if (StrUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(DictConstants.DicStatus.NORMAL.getCode());
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus(DictConstants.DicStatus.FAIL.getCode());
        }
        remoteLogService.saveLoginInfo(loginInfo, enterpriseId, sourceName, SecurityConstants.INNER);
    }
}