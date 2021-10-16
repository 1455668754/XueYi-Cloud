package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.domain.material.SysFile;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.feign.RemoteFileService;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

/**
 * 租户信息操作处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/enterprise")
public class SysEnterpriseController extends BaseController {

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteFileService remoteFileService;

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/byId/{enterpriseId}")
    public R<SysEnterprise> getInfo(@PathVariable("enterpriseId") Long enterpriseId) {
        return R.ok(enterpriseService.mainSelectEnterpriseByEnterpriseId(enterpriseId));
    }

    /**
     * 获取租户信息
     */
    @GetMapping("/profile")
    public AjaxResult profile() {
        return AjaxResult.success(enterpriseService.getEnterpriseProfile());
    }

    /**
     * logo上传
     */
    @PreAuthorize(hasPermi = "system:enterpriseAdmin:edit")
    @Log(title = "企业Logo修改", businessType = BusinessType.UPDATE)
    @PostMapping("/changeLogo")
    public AjaxResult avatar(@RequestParam("logo") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
                return AjaxResult.error("文件服务异常，请稍后再试");
            }
            String url = fileResult.getData().getUrl();
            SysEnterprise enterprise = new SysEnterprise();
            enterprise.setLogo(url);
            int rows = refreshCache(enterpriseService.mainUpdateEnterpriseLogo(enterprise));
            if (rows > 0) {
                String oldLogoUrl = loginUser.getSysEnterprise().getLogo();
                if (StringUtils.isNotEmpty(oldLogoUrl)) {
                    remoteFileService.delete(oldLogoUrl);
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);
                // 更新缓存用户头像
                loginUser.getSysEnterprise().setLogo(url);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请稍后再试");
    }

    /**
     * 普通信息修改
     */
    @PreAuthorize(hasPermi = "system:enterprise:edit")
    @Log(title = "企业资料修改", businessType = BusinessType.UPDATE)
    @PutMapping("/updateEnterprise")
    public AjaxResult updateEnterprise(@Validated @RequestBody SysEnterprise enterprise) {
        return toAjax(refreshCache(enterpriseService.mainUpdateEnterpriseMinor(enterprise)));
    }

    /**
     * 超管信息修改
     */
    @PreAuthorize(hasPermi = "system:enterpriseAdmin:edit")
    @Log(title = "企业账号修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeEnterpriseName")
    public AjaxResult changeEnterpriseName(@Validated @RequestBody SysEnterprise enterprise) {
        if (StringUtils.equals(UserConstants.NOT_UNIQUE, enterpriseService.mainCheckEnterpriseNameUnique(enterprise))) {
            return AjaxResult.error("修改失败，该企业账号名不可用，请换一个账号名！");
        }
        int i = refreshLoginCache(enterpriseService.mainUpdateEnterpriseName(enterprise));
        Collection<String> keys = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        LoginUser mine = tokenService.getLoginUser();
        //强退当前企业账户所有在线账号
        for (String key : keys) {
            LoginUser user = redisService.getCacheObject(key);
            if (mine.getEnterpriseId().equals(user.getEnterpriseId())) {
                redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + user.getToken());
            }
        }
        return toAjax(i);
    }


    /**
     * 更新当前企业的cache
     */
    private int refreshCache(int rows) {
        SysEnterprise enterprise = enterpriseService.mainSelectEnterpriseById();
        EnterpriseUtils.refreshEnterpriseCache(enterprise.getEnterpriseId(), enterprise);
        return rows;
    }

    /**
     * 更新当前企业登录验证的cache
     */
    private int refreshLoginCache(int rows) {
        SysEnterprise oldEnterprise = enterpriseService.getEnterpriseProfile();
        SysEnterprise newEnterprise = enterpriseService.mainSelectEnterpriseById();
        EnterpriseUtils.deleteLoginCache(oldEnterprise.getEnterpriseName());
        EnterpriseUtils.refreshEnterpriseCache(newEnterprise.getEnterpriseId(), newEnterprise);
        EnterpriseUtils.refreshLoginCache(newEnterprise.getEnterpriseName(), newEnterprise.getEnterpriseId());
        return rows;
    }
}