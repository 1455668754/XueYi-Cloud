package com.xueyi.system.organize.service;

import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 租户 业务层
 *
 * @author xueyi
 */
public interface ISysEnterpriseService {

    /**
     * 通过企业账号查询租户信息
     *
     * @param enterpriseName 企业账号
     * @return 租户对象
     */
    public SysEnterprise checkLoginByEnterpriseName(String enterpriseName);

    /**
     * 查询租户logo
     *
     * @return 租户对象
     */
    public SysEnterprise selectLogo();
}
