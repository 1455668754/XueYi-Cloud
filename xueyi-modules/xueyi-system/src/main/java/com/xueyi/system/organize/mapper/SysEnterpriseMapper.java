package com.xueyi.system.organize.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.organize.SysEnterprise;

import java.util.List;

/**
 * 租户表|租户信息表 数据层
 *
 * @author xueyi
 */
public interface SysEnterpriseMapper {

    /**
     * 查询所有租户信息 | 用于缓存加载
     *
     * @param enterprise 租户对象 | null
     * @return 租户对象
     */
    public List<SysEnterprise> selectEnterpriseList(SysEnterprise enterprise);

    /**
     * 通过企业账号查询租户信息
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param enterprise 租户对象 | enterpriseName 企业账号
     * @return 租户对象信息
     */
    public SysEnterprise checkLoginByEnterpriseName(SysEnterprise enterprise);

    /**
     * 查询租户信息
     * 访问控制 e 租户查询
     *
     * @param enterprise 租户对象 | null
     * @return 租户对象
     */
    @DataScope(eAlias = "e")
    public SysEnterprise selectEnterpriseById(SysEnterprise enterprise);

    /**
     * 更新Logo
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param enterprise 租户对象 | logo logo地址
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateLogo(SysEnterprise enterprise);

    /**
     * 更新租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateEnterprise(SysEnterprise enterprise);

    /**
     * 更新租户账号
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int changeEnterpriseName(SysEnterprise enterprise);

    /**
     * 校验租户账号是否唯一
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public SysEnterprise checkEnterpriseNameUnique(SysEnterprise enterprise);
}
