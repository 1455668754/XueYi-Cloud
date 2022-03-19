package com.xueyi.system.organize.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/enterprise")
public class SysEnterpriseController extends BaseController<SysEnterpriseDto, ISysEnterpriseService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "企业";
    }

    /**
     * 获取企业信息 | 内部调用
     */
    @InnerAuth
    @GetMapping("/infoById/{enterpriseId}")
    public R<SysEnterpriseDto> getInfo(@PathVariable("enterpriseId") Long enterpriseId) {
        return R.ok(baseService.selectById(enterpriseId));
    }

    /**
     * 获取当前企业信息
     */
    @GetMapping("/profile")
    public AjaxResult profile() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return AjaxResult.success(loginUser.getEnterprise());
    }

//    /**
//     * logo上传
//     */
//    @RequiresPermissions("system:enterpriseAdmin:edit")
//    @Log(title = "企业Logo修改", businessType = BusinessType.UPDATE)
//    @PostMapping("/changeLogo")
//    public AjaxResult avatar(@RequestParam("logo") MultipartFile file) throws IOException {
//        if (!file.isEmpty()) {
//            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//            R<SysFile> fileResult = remoteFileService.upload(file);
//            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
//                return AjaxResult.error("文件服务异常，请稍后再试");
//            }
//            String url = fileResult.getData().getUrl();
//            SysEnterprise enterprise = new SysEnterprise();
//            enterprise.setLogo(url);
//            int rows = refreshCache(enterpriseService.mainUpdateEnterpriseLogo(enterprise));
//            if (rows > 0) {
//                String oldLogoUrl = loginUser.getSysEnterprise().getLogo();
//                if (StringUtils.isNotEmpty(oldLogoUrl)) {
//                    remoteFileService.delete(oldLogoUrl);
//                }
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", url);
//                // 更新缓存用户头像
//                loginUser.getSysEnterprise().setLogo(url);
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
//        }
//        return AjaxResult.error("上传图片异常，请稍后再试");
//    }
//
//    /**
//     * 普通信息修改
//     */
//    @RequiresPermissions("system:enterprise:edit")
//    @Log(title = "企业资料修改", businessType = BusinessType.UPDATE)
//    @PutMapping("/updateEnterprise")
//    public AjaxResult updateEnterprise(@Validated @RequestBody SysEnterprise enterprise) {
//        return toAjax(refreshCache(enterpriseService.mainUpdateEnterpriseMinor(enterprise)));
//    }
//
//    /**
//     * 超管信息修改
//     */
//    @RequiresPermissions("system:enterpriseAdmin:edit")
//    @Log(title = "企业账号修改", businessType = BusinessType.UPDATE)
//    @PutMapping("/changeEnterpriseName")
//    public AjaxResult changeEnterpriseName(@Validated @RequestBody SysEnterprise enterprise) {
//        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), enterpriseService.mainCheckEnterpriseNameUnique(enterprise))) {
//            return AjaxResult.error("修改失败，该企业账号名不可用，请换一个账号名！");
//        }
//        int i = refreshLoginCache(enterpriseService.mainUpdateEnterpriseName(enterprise));
//        Collection<String> keys = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
//        LoginUser mine = tokenService.getLoginUser();
//        //强退当前企业账户所有在线账号
//        for (String key : keys) {
//            LoginUser user = redisService.getCacheObject(key);
//            if (mine.getEnterpriseId().equals(user.getEnterpriseId())) {
//                redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + user.getToken());
//            }
//        }
//        return toAjax(i);
//    }
}
