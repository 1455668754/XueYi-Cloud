package com.xueyi.system.organize.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 租户表|租户信息表 数据层
 *
 * @author xueyi
 */
public interface SysEnterpriseMapper {

    /**
     * 通过企业账号查询租户信息
     * 登陆前验证，无需切片控制(service/impl层在com.xueyi.authority.service)
     *
     * @param search 万用组件 | enterpriseName 企业账号
     * @return 租户对象信息
     */
    public SysEnterprise checkLoginByEnterpriseName(SysSearch search);

    /**
     * 查询租户logo
     * 访问控制 e 租户查询
     *
     * @param search 万用组件 | null
     * @return 租户对象
     */
    @DataScope(eAlias = "e")
    public SysEnterprise selectLogo(SysSearch search);

    /**
     * 查询租户信息
     * 访问控制 e 租户查询
     *
     * @param search 万用组件 | null
     * @return 租户对象
     */
    @DataScope(eAlias = "e")
    public SysEnterprise selectEnterpriseById(SysSearch search);

    /**
     * 更新Logo
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | logoUrl logo地址
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateLogo(SysSearch search);

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
