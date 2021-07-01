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
     * @param enterprise 租户对象 | enterpriseName 企业账号
     * @return 租户对象
     */
    public SysEnterprise checkLoginByEnterpriseName(SysEnterprise enterprise);

    /**
     * 查询租户信息
     *
     * @return 租户对象
     */
    public SysEnterprise selectEnterpriseById();

    /**
     * 更新Logo
     *
     * @param enterprise 租户对象 | logo logo地址
     * @return 结果
     */
    public int updateLogo(SysEnterprise enterprise);

    /**
     * 更新租户信息
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    public int updateEnterprise(SysEnterprise enterprise);

    /**
     * 更新租户账号
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    public int changeEnterpriseName(SysEnterprise enterprise);

    /**
     * 校验租户账号是否唯一
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    public String checkEnterpriseNameUnique(SysEnterprise enterprise);
}
