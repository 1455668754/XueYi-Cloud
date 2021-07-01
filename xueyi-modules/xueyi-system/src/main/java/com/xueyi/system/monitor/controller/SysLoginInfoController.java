package com.xueyi.system.monitor.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.ip.IpUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.monitor.domain.SysLoginInfo;
import com.xueyi.system.monitor.service.ISysLoginInfoService;

/**
 * 系统访问记录
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/loginInfo")
public class SysLoginInfoController extends BaseController {
    @Autowired
    private ISysLoginInfoService loginInfoService;

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize(hasPermi = "system:loginInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo loginInfo) {
        startPage();
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:loginInfo:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginInfo loginInfo) throws IOException {
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<SysLoginInfo>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize(hasPermi = "system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    @PreAuthorize(hasPermi = "system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        loginInfoService.cleanLoginInfo();
        return AjaxResult.success();
    }

    @PostMapping
    public AjaxResult add(@RequestParam("enterpriseName") String enterpriseName, @RequestParam("userName") String userName, @RequestParam("status") String status,
                          @RequestParam("message") String message) {
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        // 封装对象
        SysLoginInfo loginInfo = new SysLoginInfo();
        SysEnterprise checkEnterprise = new SysEnterprise();
        checkEnterprise.setEnterpriseName(enterpriseName);
        SysEnterprise enterprise = enterpriseService.checkLoginByEnterpriseName(checkEnterprise);

        SysUser checkUser = new SysUser();
        checkUser.setEnterpriseId(enterprise.getEnterpriseId());
        checkUser.setUserName(userName);
        if(enterprise != null && StringUtils.isNotNull(enterprise.getEnterpriseId())){
            SysUser user = userService.checkUserByUserName(checkUser);
            loginInfo.setUserId(user.getUserId());
            loginInfo.setEnterpriseId(enterprise.getEnterpriseId());
        }else {
            loginInfo.setEnterpriseId(0L);
        }
        loginInfo.setEnterpriseName(enterpriseName);
        loginInfo.setUserName(userName);
        loginInfo.setIpaddr(ip);
        loginInfo.setMsg(message);
        // 日志状态
        if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status)) {
            loginInfo.setStatus("0");
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus("1");
        }
        return toAjax(loginInfoService.insertLoginInfo(loginInfo));
    }
}
