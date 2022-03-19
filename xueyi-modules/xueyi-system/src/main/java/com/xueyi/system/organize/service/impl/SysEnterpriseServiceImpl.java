package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.organize.manager.SysEnterpriseManager;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 企业管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysEnterpriseServiceImpl extends BaseServiceImpl<SysEnterpriseDto, SysEnterpriseManager, SysEnterpriseMapper> implements ISysEnterpriseService {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto selectByName(String name) {
        return baseManager.selectByName(name);
    }
}
