package com.xueyi.system.organize.controller;

import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户信息操作处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/enterprise")
public class SysEnterpriseController{

    @Autowired
    private ISysEnterpriseService enterpriseService;

    /**
     * 获取logo信息
     */
    @PreAuthorize(hasPermi = "system:enterprise:logo")
    @GetMapping("/logo")
    public AjaxResult logo()
    {
        return AjaxResult.success(enterpriseService.selectLogo());
    }
}
