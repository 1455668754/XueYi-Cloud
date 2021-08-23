package com.xueyi.system.organize.service;

import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.source.Source;

import java.util.List;

/**
 * 租户 业务层
 *
 * @author xueyi
 */
public interface ISysEnterpriseService {

    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    public List<Source> selectLoadDataSources(Source source);

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
