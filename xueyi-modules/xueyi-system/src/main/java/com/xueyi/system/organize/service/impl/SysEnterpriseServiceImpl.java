package com.xueyi.system.organize.service.impl;

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
    public SysEnterprise checkLoginByEnterpriseName(String enterpriseName) {
        SysSearch search = new SysSearch();
        search.getSearch().put("enterpriseName", enterpriseName);
        return enterpriseMapper.checkLoginByEnterpriseName(search);//@param search 万用组件 | enterpriseName 企业账号
    }

    /**
     * 查询租户logo
     *
     * @return 租户对象
     */
    public SysEnterprise selectLogo() {
        return enterpriseMapper.selectLogo(new SysSearch());//@param search 万用组件 | null
    }

    /**
     * 查询租户信息
     *
     * @return 租户对象
     */
    public SysEnterprise selectEnterpriseById() {
        return enterpriseMapper.selectEnterpriseById(new SysSearch());//@param search 万用组件 | null
    }

    /**
     * 更新Logo
     *
     * @param logoUrl logo地址
     * @return 结果
     */
    public int updateLogo(String logoUrl){
        SysSearch search = new SysSearch();
        search.getSearch().put("logoUrl", logoUrl);
        return enterpriseMapper.updateLogo(search);
    }
}
