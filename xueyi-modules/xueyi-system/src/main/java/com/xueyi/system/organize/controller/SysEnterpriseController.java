package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.RemoteFileService;
import com.xueyi.system.api.material.SysFile;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private RemoteFileService remoteFileService;
    /**
     * 获取logo信息
     */
    @GetMapping("/logo")
    public AjaxResult logo() {
        return AjaxResult.success(enterpriseService.selectLogo());
    }

    /**
     * 获取租户信息
     */
    @PreAuthorize(hasPermi = "system:enterprise:list")
    @GetMapping("/profile")
    public AjaxResult profile() {
        return AjaxResult.success(enterpriseService.selectEnterpriseById());
    }

    /**
     * logo上传
     */
    @PreAuthorize(hasPermi = "system:enterpriseAdmin:edit")
    @Log(title = "企业Logo修改", businessType = BusinessType.UPDATE)
    @PostMapping("/changeLogo")
    public AjaxResult avatar(@RequestParam("logo") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData()))
            {
                return AjaxResult.error("文件服务异常，请稍后再试");
            }
            String url = fileResult.getData().getUrl();
            if (enterpriseService.updateLogo(url) > 0)
            {
                String oldLogoUrl = loginUser.getSysEnterprise().getLogo();
                if(StringUtils.isNotEmpty(oldLogoUrl)){
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
        return toAjax(enterpriseService.updateEnterprise(enterprise));
    }

    /**
     * 超管信息修改
     */
    @PreAuthorize(hasPermi = "system:enterpriseAdmin:edit")
    @Log(title = "企业账号修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeEnterpriseName")
    public AjaxResult changeEnterpriseName(@Validated @RequestBody SysEnterprise enterprise) {
        return toAjax(enterpriseService.changeEnterpriseName(enterprise));
    }
}