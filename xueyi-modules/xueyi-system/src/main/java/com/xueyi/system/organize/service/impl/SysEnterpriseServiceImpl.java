package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SysEnterpriseServiceImpl implements ISysEnterpriseService {

    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    @Autowired
    private RedisService redisService;

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
     * 查询企业信息
     *
     * @return 企业对象
     */
    @Override
    public SysEnterprise mainSelectEnterpriseById() {
        return getEnterpriseProfile();
    }

    /**
     * 更新Logo
     *
     * @param enterprise 企业对象 | logo logoUrl
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseLogo(SysEnterprise enterprise) {
        int rows = enterpriseMapper.mainUpdateEnterpriseLogo(enterprise);
        if (rows > 0) {
            refreshCache();
        }
        return rows;
    }

    /**
     * 更新企业次要信息
     *
     * @param enterprise 企业对象
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseMinor(SysEnterprise enterprise) {
        int rows = enterpriseMapper.mainUpdateEnterpriseMinor(enterprise);
        if (rows > 0) {
            refreshCache();
        }
        return rows;
    }

    /**
     * 修改企业账号
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @Override
    public int mainUpdateEnterpriseName(SysEnterprise enterprise) {
        int rows = enterpriseMapper.mainUpdateEnterpriseName(enterprise);
        if (rows > 0) {
            refreshCache();
            refreshLoginCache();
        }
        return rows;
    }

    /**
     * 校验企业账号是否唯一
     *
     * @param enterprise 企业对象 | enterpriseName 企业账号
     * @return 结果
     */
    @Override
    public String mainCheckEnterpriseNameUnique(SysEnterprise enterprise) {
        return enterpriseMapper.mainCheckEnterpriseNameUnique(enterprise) == null ? UserConstants.UNIQUE : UserConstants.NOT_UNIQUE;
    }

    /**
     * 更新当前企业的cache
     */
    private void refreshCache() {
        SysEnterprise enterprise = enterpriseMapper.mainSelectEnterpriseById(new SysEnterprise());
        EnterpriseUtils.refreshEnterpriseCache(enterprise.getEnterpriseId(), enterprise);
    }

    /**
     * 更新当前企业登录验证的cache
     */
    private void refreshLoginCache() {
        SysEnterprise oldEnterprise = getEnterpriseProfile();
        SysEnterprise newEnterprise = enterpriseMapper.mainSelectEnterpriseById(new SysEnterprise());
        EnterpriseUtils.deleteLoginCache(oldEnterprise.getEnterpriseName());
        EnterpriseUtils.refreshLoginCache(newEnterprise.getEnterpriseName(), newEnterprise.getEnterpriseId());
    }

    /**
     * 查询当前账户的企业信息
     *
     * @return 参数键值
     */
    private SysEnterprise getEnterpriseProfile() {
        return redisService.getCacheObject(EnterpriseUtils.getEnterpriseCacheKey(SecurityUtils.getEnterpriseId()));
    }
}