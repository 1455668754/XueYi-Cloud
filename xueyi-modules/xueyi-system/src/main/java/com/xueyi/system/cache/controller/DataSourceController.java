package com.xueyi.system.cache.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.cache.service.ISysCacheInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/dataSource")
public class DataSourceController extends BaseController {

    @Autowired
    private ISysCacheInitService cacheInitService;

    /**
     * 根据策略Id刷新策略缓存
     */
    @InnerAuth
    @GetMapping("/refreshSource/{strategyId}")
    public R<Boolean> refreshSource(@PathVariable("strategyId") Long strategyId) {
        cacheInitService.refreshSourceCacheByStrategyId(strategyId);
        return R.ok(true);
    }
}
