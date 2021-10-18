package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.domain.monitor.SysLoginInfo;
import com.xueyi.system.monitor.service.ISysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    @RequiresPermissions("system:loginInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo loginInfo) {
        startPage();
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:loginInfo:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginInfo loginInfo) throws IOException {
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<SysLoginInfo>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    @RequiresPermissions("system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysLoginInfo loginInfo) {
        return toAjax(loginInfoService.deleteLoginInfoByIds(loginInfo));
    }

    @RequiresPermissions("system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        loginInfoService.cleanLoginInfo();
        return AjaxResult.success();
    }

    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginInfo loginInfo) {
        return toAjax(loginInfoService.insertLoginInfo(loginInfo.getSourceName(), loginInfo));
    }
}
