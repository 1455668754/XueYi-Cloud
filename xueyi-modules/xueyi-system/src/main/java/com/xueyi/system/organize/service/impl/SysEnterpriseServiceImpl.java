package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.api.utilTool.SysSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户信息 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysEnterpriseServiceImpl implements ISysEnterpriseService {
    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    /**
     * 通过企业账号查询租户信息
     *
     * @param enterpriseName 企业账号
     * @return 租户对象
     */
    @Override
    public SysEnterprise checkLoginByEnterpriseName(String enterpriseName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseName", enterpriseName);
        return enterpriseMapper.checkLoginByEnterpriseName(search);//@param search 万用组件 | enterpriseName 企业账号
    }

    /**
     * 查询租户信息
     *
     * @return 租户对象
     */
    @Override
    public SysEnterprise selectEnterpriseById() {
        return enterpriseMapper.selectEnterpriseById(new SysSearch());//@param search 万用组件 | null
    }

    /**
     * 更新Logo
     *
     * @param logoUrl logo地址
     * @return 结果
     */
    @Override
    public int updateLogo(String logoUrl) {
        SysSearch search = new SysSearch();
        search.getSearch().put("logoUrl", logoUrl);
        return enterpriseMapper.updateLogo(search);
    }

    /**
     * 更新租户信息
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @Override
    public int updateEnterprise(SysEnterprise enterprise) {
        return enterpriseMapper.updateEnterprise(enterprise);
    }

    /**
     * 更新租户账号
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @Override
    public int changeEnterpriseName(SysEnterprise enterprise) {
        return enterpriseMapper.changeEnterpriseName(enterprise);
    }

    /**
     * 校验租户账号是否唯一
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @Override
    public String checkEnterpriseNameUnique(SysEnterprise enterprise) {
        return enterpriseMapper.checkEnterpriseNameUnique(enterprise) == null ? UserConstants.UNIQUE:UserConstants.NOT_UNIQUE;
    }
}
