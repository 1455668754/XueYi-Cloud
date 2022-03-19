package com.xueyi.system.monitor.service.impl;

import org.springframework.stereotype.Service;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.monitor.domain.SysUserOnline;
import com.xueyi.system.monitor.service.ISysUserOnlineService;

/**
 * 在线用户 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户账号查询信息
     *
     * @param userName 用户账号
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUserName())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户账号查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户账号
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUserName())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user)) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUserName());
        sysUserOnline.setUserNick(user.getUser().getNickName());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginTime(user.getLoginTime());
        return sysUserOnline;
    }
}
