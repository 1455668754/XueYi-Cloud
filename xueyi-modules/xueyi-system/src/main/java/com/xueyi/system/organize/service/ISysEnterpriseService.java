package com.xueyi.system.organize.service;

import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;

import java.util.List;

/**
 * 企业 业务层
 *
 * @author xueyi
 */
public interface ISysEnterpriseService {

    /**
     * 根据企业账号查询账号信息
     *
     * @param enterpriseName 企业账号
     * @return 数据源组集合
     */
    public SysEnterprise mainGetEnterpriseProfileByEnterpriseName(String enterpriseName);

    /**
     * 根据企业Id查询企业信息
     *
     * @param enterpriseId 企业Id
     * @return 企业对象
     */
    public SysEnterprise mainSelectEnterpriseByEnterpriseId(Long enterpriseId);

    /**
     * 查询企业信息
     *
     * @return 企业对象
     */
    public SysEnterprise mainSelectEnterpriseById();

    /**
     * 更新Logo
     *
     * @param enterprise 企业对象 | logo logoUrl
     * @return 结果
     */
    public int mainUpdateEnterpriseLogo(SysEnterprise enterprise);

    /**
     * 更新企业次要信息
     *
     * @param enterprise 企业对象
     * @return 结果
     */
    public int mainUpdateEnterpriseMinor(SysEnterprise enterprise);

    /**
     * 修改企业账号
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    public int mainUpdateEnterpriseName(SysEnterprise enterprise);

    /**
     * 校验企业账号是否唯一
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    public String mainCheckEnterpriseNameUnique(SysEnterprise enterprise);

    /**
     * 加载企业缓存数据
     */
    public void loadingEnterpriseCache();

    /**
     * 清空企业缓存数据
     */
    public void clearEnterpriseCache();

    /**
     * 重置企业缓存数据
     */
    public void resetEnterpriseCache();
}
