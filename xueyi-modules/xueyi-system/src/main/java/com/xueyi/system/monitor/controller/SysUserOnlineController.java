package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.monitor.domain.SysUserOnline;
import com.xueyi.system.monitor.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/online")
public class SysUserOnlineController extends BaseController {

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @RequiresPermissions("monitor:online:list")
    @GetMapping("/list")
    public AjaxResult list(String ipaddr, String userName) {
        Collection<String> keys = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        LoginUser mine = tokenService.getLoginUser();
        for (String key : keys) {
            LoginUser user = redisService.getCacheObject(key);
            if (mine.getEnterpriseId().equals(user.getEnterpriseId())) {
                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                    if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUserName())) {
                        userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                    }
                } else if (StringUtils.isNotEmpty(ipaddr)) {
                    if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                        userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                    }
                } else if (StringUtils.isNotEmpty(userName)) {
                    if (StringUtils.equals(userName, user.getUserName())) {
                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                    }
                } else {
                    userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
                }
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return AjaxResult.success();
    }
}
