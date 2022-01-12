package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 企业信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class SysEnterpriseServiceImpl implements ISysEnterpriseService {

    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    /**
     * 根据企业Id查询企业信息
     *
     * @param enterpriseId 企业Id
     * @return 企业对象
     */
    @Override
    public SysEnterprise mainSelectEnterpriseByEnterpriseId(Long enterpriseId) {
        return enterpriseMapper.mainSelectEnterpriseByEnterpriseId(enterpriseId);
    }

    /**
     * 查询当前企业信息
     *
     * @return 企业对象
     */
    @Override
    public SysEnterprise mainSelectEnterpriseById() {
        return enterpriseMapper.mainSelectEnterpriseById(new SysEnterprise());
    }

    /**
     * 查询当前账户的企业信息
     *
     * @return 企业对象
     */
    @Override
    public SysEnterprise getEnterpriseProfile() {
        return EnterpriseUtils.getEnterpriseByEnterpriseId(SecurityUtils.getEnterpriseId());
    }

    /**
     * 更新Logo
     *
     * @param enterprise 企业对象 | logo logoUrl
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseLogo(SysEnterprise enterprise) {
        return enterpriseMapper.mainUpdateEnterpriseLogo(enterprise);
    }

    /**
     * 更新企业次要信息
     *
     * @param enterprise 企业对象
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseMinor(SysEnterprise enterprise) {
        return enterpriseMapper.mainUpdateEnterpriseMinor(enterprise);
    }

    /**
     * 修改企业账号
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseName(SysEnterprise enterprise) {
        return enterpriseMapper.mainUpdateEnterpriseName(enterprise);
    }

    /**
     * 校验企业账号是否唯一
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @Override
    public String mainCheckEnterpriseNameUnique(SysEnterprise enterprise) {
        return enterpriseMapper.mainCheckEnterpriseNameUnique(enterprise) == null ? BaseConstants.Check.UNIQUE.getCode() : BaseConstants.Check.NOT_UNIQUE.getCode();
    }
}