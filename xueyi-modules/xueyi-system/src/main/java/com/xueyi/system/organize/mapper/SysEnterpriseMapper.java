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
    @DataScope(enterpriseAlias = "e")
    public SysEnterprise selectLogo(SysSearch search);
}
