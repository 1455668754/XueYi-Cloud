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
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.feign.RemoteFileService;
import com.xueyi.system.api.domain.material.SysFile;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
     * 获取指定企业账号的数据源
     */
    @GetMapping("/loadDataSources/{enterpriseId}")
    public R<List<Source>> info(@PathVariable("enterpriseId") Long enterpriseId) {
        Source source = new Source();
        source.setEnterpriseId(enterpriseId);
        List<Source> sources = enterpriseService.selectLoadDataSources(source);
        return R.ok(sources);
    }

    /**
     * 获取租户信息
     */
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
            if (enterpriseService.updateLogo(enterprise) > 0) {
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
        return toAjax(enterpriseService.updateEnterprise(enterprise));
    }

    /**
     * 超管信息修改
     */
    @PreAuthorize(hasPermi = "system:enterpriseAdmin:edit")
    @Log(title = "企业账号修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeEnterpriseName")
    public AjaxResult changeEnterpriseName(@Validated @RequestBody SysEnterprise enterprise) {
        if (StringUtils.equals(UserConstants.NOT_UNIQUE, enterpriseService.checkEnterpriseNameUnique(enterprise))) {
            return AjaxResult.error("修改失败，该企业账号名不可用，请换一个账号名！");
        }
        int i = enterpriseService.changeEnterpriseName(enterprise);
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
     * 根据企业 key 新增|更新 cache
     *
     * @param newEnterprise 企业对象
     */
    @GetMapping("/refreshEnterpriseCache")
    public R<Boolean> refreshEnterpriseCache(SysEnterprise newEnterprise) {
        enterpriseService.refreshEnterpriseCache(newEnterprise);
        return R.ok(true);
    }

    /**
     * 刷新指定企业 cache 的 key
     *
     * @param oldEnterpriseName 原企业账号
     * @param newEnterprise     企业对象
     */
    @GetMapping("/refreshEnterpriseKey/{oldEnterpriseName}")
    public R<Boolean> refreshEnterpriseKey(@PathVariable("oldEnterpriseName") String oldEnterpriseName, SysEnterprise newEnterprise) {
        enterpriseService.refreshEnterpriseKey(oldEnterpriseName, newEnterprise);
        return R.ok(true);
    }

    /**
     * 重置企业缓存数据
     */
    @GetMapping("/resetEnterpriseCache")
    public R<Boolean> resetEnterpriseCache() {
        enterpriseService.resetEnterpriseCache();
        return R.ok(true);
    }
}