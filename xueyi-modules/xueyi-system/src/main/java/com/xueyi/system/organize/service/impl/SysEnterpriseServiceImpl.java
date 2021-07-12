package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.source.mapper.DataSourceMapper;
import com.xueyi.system.api.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户信息 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysEnterpriseServiceImpl implements ISysEnterpriseService {

    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    public List<Source> selectLoadDataSources(Source source){
        return dataSourceMapper.selectLoadDataSources(source);
    }

    /**
     * 通过企业账号查询租户信息
     *
     * @param enterprise 租户对象 | enterpriseName 企业账号
     * @return 租户对象
     */
    @Override
    public SysEnterprise checkLoginByEnterpriseName(SysEnterprise enterprise) {
        return enterpriseMapper.checkLoginByEnterpriseName(enterprise);
    }

    /**
     * 查询租户信息
     *
     * @return 租户对象
     */
    @Override
    public SysEnterprise selectEnterpriseById() {
        return enterpriseMapper.selectEnterpriseById(new SysEnterprise());
    }

    /**
     * 更新Logo
     *
     * @param enterprise 租户对象 | logo logo地址
     * @return 结果
     */
    @Override
    public int updateLogo(SysEnterprise enterprise) {
        return enterpriseMapper.updateLogo(enterprise);
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
        return enterpriseMapper.checkEnterpriseNameUnique(enterprise) == null ? UserConstants.UNIQUE : UserConstants.NOT_UNIQUE;
    }
}
