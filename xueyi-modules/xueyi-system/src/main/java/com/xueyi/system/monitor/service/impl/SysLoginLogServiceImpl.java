package com.xueyi.system.monitor.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.monitor.manager.SysLoginLogManager;
import com.xueyi.system.monitor.mapper.SysLoginLogMapper;
import com.xueyi.system.monitor.service.ISysLoginLogService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 访问日志管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysLoginLogServiceImpl extends BaseServiceImpl<SysLoginLogDto, SysLoginLogManager, SysLoginLogMapper> implements ISysLoginLogService {

    /**
     * 新增系统登录日志
     *
     * @param loginLog 访问日志对象 | sourceName 数据源名称
     */
    @Override
    public int insert(SysLoginLogDto loginLog) {
        return baseManager.insert(loginLog);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginLog() {
        baseManager.cleanLoginLog(SecurityUtils.getEnterpriseId());
    }
}
