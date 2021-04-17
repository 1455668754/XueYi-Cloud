package com.xueyi.system.organize.service;

import com.xueyi.system.api.organize.SysEnterprise;

/**
 * 租户 业务层
 *
 * @author xueyi
 */
public interface ISysEnterpriseService
{

    /**
     * 查询租户logo
     *
     * @return 租户对象
     */
    public SysEnterprise selectLogo();
}
