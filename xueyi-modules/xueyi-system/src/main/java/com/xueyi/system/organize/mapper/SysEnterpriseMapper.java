package com.xueyi.system.organize.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.organize.SysEnterprise;

import java.util.List;

/**
 * 企业表|企业信息表 数据层
 *
 * @author xueyi
 */
public interface SysEnterpriseMapper {

    /**
     * 查询所有企业信息 | 用于缓存加载
     *
     * @return 企业对象
     */
    public List<SysEnterprise> mainSelectEnterpriseCacheList();

    /**
     * 根据企业Id查询企业信息
     *
     * @param enterpriseId 企业Id
     * @return 企业对象
     */
    public SysEnterprise mainSelectEnterpriseByEnterpriseId(Long enterpriseId);

    /**
     * 查询企业信息
     * 访问控制 e 企业查询
     *
     * @param enterprise 企业对象 | null
     * @return 企业对象
     */
    @DataScope(eAlias = "e")
    public SysEnterprise mainSelectEnterpriseById(SysEnterprise enterprise);

    /**
     * 更新Logo
     * 访问控制 empty 企业更新（无前缀）
     *
     * @param enterprise 企业对象 | logo logoUrl
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateEnterpriseLogo(SysEnterprise enterprise);

    /**
     * 更新企业次要信息
     * 访问控制 empty 企业更新（无前缀）
     *
     * @param enterprise 企业对象
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateEnterpriseMinor(SysEnterprise enterprise);

    /**
     * 修改企业账号
     * 访问控制 empty 企业更新（无前缀）
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateEnterpriseName(SysEnterprise enterprise);

    /**
     * 校验企业账号是否唯一
     * 访问控制 empty 企业更新（无前缀）
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public SysEnterprise mainCheckEnterpriseNameUnique(SysEnterprise enterprise);
}
