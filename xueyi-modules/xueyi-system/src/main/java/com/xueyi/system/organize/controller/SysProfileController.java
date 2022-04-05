package com.xueyi.system.organize.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.file.api.domain.SysFile;
import com.xueyi.file.api.feign.RemoteFileService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BasisController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private ISysUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        userService.userDesensitized(loginUser.getUser());
        return AjaxResult.success(loginUser.getUser());
    }

    /**
     * 修改用户
     */
    @PutMapping
    @Log(title = "个人信息管理 - 基本信息修改", businessType = BusinessType.UPDATE)
    public AjaxResult editProfile(@Validated @RequestBody SysUserDto user) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (userService.updateUserProfile(loginUser.getUserId(), user.getNickName(), user.getSex(), user.getProfile()) > 0) {
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setSex(user.getSex());
            loginUser.getUser().setProfile(user.getProfile());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员！");
    }

    /**
     * 修改账号
     */
    @PutMapping("/userName")
    @Log(title = "个人信息管理 - 修改账号", businessType = BusinessType.UPDATE)
    public AjaxResult editUserName(String userName) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StrUtil.isEmpty(userName))
            return AjaxResult.error("不能设置为空账号！");
        else if (StrUtil.equals(userName, loginUser.getUser().getUserName()))
            return AjaxResult.error("该账号为当前使用账号，无需更换！");
        else if (userService.checkUserNameUnique(loginUser.getUserId(), userName))
            return AjaxResult.error("该账号已被使用！");
        if (userService.updateUserName(loginUser.getUserId(), userName) > 0) {
            // 更新缓存
            loginUser.getUser().setUserName(userName);
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("更绑邮箱异常，请联系管理员！");
    }

    /**
     * 更绑邮箱
     */
    @PutMapping("/email")
    @Log(title = "个人信息管理 - 更绑邮箱", businessType = BusinessType.UPDATE)
    public AjaxResult editEmail(String email) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StrUtil.isEmpty(email))
            return AjaxResult.error("不能设置为空邮箱！");
        else if (StrUtil.equals(email, loginUser.getUser().getEmail()))
            return AjaxResult.error("该邮箱为当前使用邮箱，无需更换！");
        else if (userService.checkEmailUnique(loginUser.getUserId(), email))
            return AjaxResult.error("该邮箱已被使用！");
        if (userService.updateEmail(loginUser.getUserId(), email) > 0) {
            // 更新缓存
            loginUser.getUser().setEmail(email);
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("更绑邮箱异常，请联系管理员！");
    }

    /**
     * 更绑手机号
     */
    @PutMapping("/phone")
    @Log(title = "个人信息管理 - 更绑手机号", businessType = BusinessType.UPDATE)
    public AjaxResult editPhone(String phone) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StrUtil.isEmpty(phone))
            return AjaxResult.error("不能设置为空手机号！");
        else if (StrUtil.equals(phone, loginUser.getUser().getPhone()))
            return AjaxResult.error("该邮箱为当前使用手机号，无需更换！");
        else if (userService.checkPhoneUnique(loginUser.getUserId(), phone))
            return AjaxResult.error("该手机号已被使用！");
        if (userService.updatePhone(loginUser.getUserId(), phone) > 0) {
            // 更新缓存
            loginUser.getUser().setPhone(phone);
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("更绑手机号异常，请联系管理员！");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @Log(title = "个人信息管理 - 重置密码", businessType = BusinessType.UPDATE)
    public AjaxResult editPassword(String oldPassword, String newPassword) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String password = loginUser.getUser().getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
            return AjaxResult.error("修改失败，旧密码错误！");
        if (SecurityUtils.matchesPassword(newPassword, password))
            return AjaxResult.error("新旧密码不能相同！");
        if (userService.resetUserPassword(loginUser.getUserId(), SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员！");
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    @Log(title = "个人信息管理 - 修改头像", businessType = BusinessType.UPDATE)
    public AjaxResult editAvatar(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getResult()))
                return AjaxResult.error("文件服务异常，请联系管理员！");
            String url = fileResult.getResult().getUrl();
            if (userService.updateUserAvatar(SecurityUtils.getUserId(), url) > 0) {
                String oldAvatarUrl = loginUser.getUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatarUrl))
                    remoteFileService.delete(oldAvatarUrl);
                AjaxResult ajax = AjaxResult.success();
                ajax.put(AjaxResult.URL_TAG, url);
                // 更新缓存 - 用户头像
                loginUser.getUser().setAvatar(url);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员！");
    }
}
