package com.xueyi.system.monitor.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.monitor.service.ISysLoginLogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 访问日志管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/loginLog")
public class SysLoginLogController extends BaseController<SysLoginLogDto, ISysLoginLogService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "访问日志";
    }

    /**
     * 新增访问日志 | 内部调用
     */
    @InnerAuth
    @PostMapping
    public R<Boolean> addInner(@RequestBody SysLoginLogDto loginInfo) {
        baseService.insert(loginInfo);
        return R.ok();
    }

    /**
     * 查询系统访问记录列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_LOGIN_LOG_LIST)
    public AjaxResult list(SysLoginLogDto loginLog) {
        return super.list(loginLog);
    }

    /**
     * 系统访问记录导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_LOGIN_LOG_EXPORT)
    @Log(title = "访问日志", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysLoginLogDto loginLog) {
        super.export(response, loginLog);
    }

    /**
     * 系统访问记录批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_LOGIN_LOG_DELETE)
    @Log(title = "访问日志", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    @DeleteMapping("/clean")
    @RequiresPermissions(Auth.SYS_LOGIN_LOG_DELETE)
    @Log(title = "访问日志", businessType = BusinessType.CLEAN)
    public AjaxResult clean() {
        baseService.cleanLoginLog();
        return AjaxResult.success();
    }

}
