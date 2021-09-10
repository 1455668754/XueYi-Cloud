package com.xueyi.system.organize.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.source.mapper.DataSourceMapper;
import com.xueyi.system.api.domain.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 企业信息 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysEnterpriseServiceImpl implements ISysEnterpriseService {

    @Autowired
    private SysEnterpriseMapper enterpriseMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingEnterpriseCache();
    }

    /**
     * 根据企业账号查询账号信息
     *
     * @param enterpriseName 企业账号
     * @return 数据源组集合
     */
    @Override
    public SysEnterprise mainGetEnterpriseProfileByEnterpriseName(String enterpriseName) {
        Long enterpriseId = redisService.getCacheObject(EnterpriseUtils.getLoginCacheKey(enterpriseName));
        return ObjectUtil.equals(enterpriseId, null) ? null : redisService.getCacheObject(EnterpriseUtils.getEnterpriseCacheKey(enterpriseId));
    }

    /**
     * 查询数据源列表
     *
     * @param source 数据源组
     * @return 数据源组集合
     */
    @Override
    public List<Source> selectLoadDataSources(Source source) {
        return dataSourceMapper.selectLoadDataSources(source);
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
     * 加载企业缓存数据
     */
    @Override
    public void loadingEnterpriseCache() {
        List<SysEnterprise> enterprisesList = enterpriseMapper.mainSelectEnterpriseCacheList();
        for (SysEnterprise enterprise : enterprisesList) {
            redisService.setCacheObject(EnterpriseUtils.getEnterpriseCacheKey(enterprise.getEnterpriseId()), enterprise);
            redisService.setCacheObject(EnterpriseUtils.getStrategyCacheKey(enterprise.getEnterpriseId()), enterprise.getStrategyId());
            redisService.setCacheObject(EnterpriseUtils.getLoginCacheKey(enterprise.getEnterpriseName()), enterprise.getEnterpriseId());
        }
    }

    /**
     * 清空企业缓存数据
     */
    @Override
    public void clearEnterpriseCache() {
        Collection<String> keys = redisService.keys(Constants.SYS_ENTERPRISE_KEY + "*");
        redisService.deleteObject(keys);
        Collection<String> loginKeys = redisService.keys(Constants.LOGIN_ENTERPRISE_KEY + "*");
        redisService.deleteObject(loginKeys);
    }

    /**
     * 重置企业缓存数据
     */
    @Override
    public void resetEnterpriseCache() {
        clearEnterpriseCache();
        loadingEnterpriseCache();
    }

    /**
     * 更新当前企业的cache
     */
    private void refreshCache() {
        SysEnterprise enterprise = enterpriseMapper.mainSelectEnterpriseById(new SysEnterprise());
        redisService.setCacheObject(EnterpriseUtils.getEnterpriseCacheKey(enterprise.getEnterpriseId()), enterprise);
    }

    /**
     * 更新当前企业登录验证的cache
     */
    private void refreshLoginCache() {
        SysEnterprise oldEnterprise = getEnterpriseProfile();
        SysEnterprise newEnterprise = enterpriseMapper.mainSelectEnterpriseById(new SysEnterprise());
        redisService.deleteObject(EnterpriseUtils.getLoginCacheKey(oldEnterprise.getEnterpriseName()));
        redisService.setCacheObject(EnterpriseUtils.getLoginCacheKey(newEnterprise.getEnterpriseName()), newEnterprise.getEnterpriseId());
    }

    /**
     * 查询当前账户的企业信息
     *
     * @return 参数键值
     */
    private SysEnterprise getEnterpriseProfile() {
        return redisService.getCacheObject(EnterpriseUtils.getEnterpriseCacheKey(tokenService.getLoginUser().getEnterpriseId()));
    }
}