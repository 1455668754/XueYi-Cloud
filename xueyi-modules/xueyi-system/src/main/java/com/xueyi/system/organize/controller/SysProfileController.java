package com.xueyi.system.organize.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.feign.RemoteFileService;
import com.xueyi.system.api.domain.material.SysFile;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.organize.service.ISysUserService;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = tokenService.getLoginUser();
        return AjaxResult.success(loginUser.getSysUser());
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        LoginUser loginUser = tokenService.getLoginUser();
        SysUser sysUser = loginUser.getSysUser();
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            loginUser.getSysUser().setNickName(user.getNickName());
            loginUser.getSysUser().setPhone(user.getPhone());
            loginUser.getSysUser().setEmail(user.getEmail());
            loginUser.getSysUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        Long userId = SecurityUtils.getUserId();
        SysUser checkUser = new SysUser();
        checkUser.setUserName(SecurityUtils.getUserName());
        SysUser user = userService.selectUserByUserName(checkUser);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        SysUser upUser = new SysUser();
        upUser.setUserId(userId);
        upUser.setPassword(SecurityUtils.encryptPassword(newPassword));
        if (userService.resetUserPwd(upUser) > 0) {
            // 更新缓存用户密码
            LoginUser loginUser = tokenService.getLoginUser();
            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
                return AjaxResult.error("文件服务异常，请联系管理员");
            }
            String url = fileResult.getData().getUrl();
            SysUser user = new SysUser();
            user.setUserId(loginUser.getUserId());
            user.setAvatar(url);
            if (userService.updateUserAvatar(user)) {
                String oldAvatarUrl = loginUser.getSysUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatarUrl)) {
                    remoteFileService.delete(oldAvatarUrl);
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);
                // 更新缓存用户头像
                loginUser.getSysUser().setAvatar(url);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}