package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.Constants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.redis.service.RedisService;
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
        int rows = enterpriseMapper.updateLogo(enterprise);
        if (rows > 0) {
            SysEnterprise newEnterprise = enterpriseMapper.selectEnterpriseById(new SysEnterprise());
            refreshEnterpriseCache(newEnterprise);
        }
        return rows;
    }

    /**
     * 更新租户信息
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @Override
    public int updateEnterprise(SysEnterprise enterprise) {
        int rows = enterpriseMapper.updateEnterprise(enterprise);
        if (rows > 0) {
            SysEnterprise newEnterprise = enterpriseMapper.selectEnterpriseById(new SysEnterprise());
            refreshEnterpriseCache(newEnterprise);
        }
        return rows;
    }

    /**
     * 更新租户账号
     *
     * @param enterprise 租户对象
     * @return 结果
     */
    @Override
    public int changeEnterpriseName(SysEnterprise enterprise) {
        int rows = enterpriseMapper.changeEnterpriseName(enterprise);
        if (rows > 0) {
            SysEnterprise newEnterprise = enterpriseMapper.selectEnterpriseById(new SysEnterprise());
            refreshEnterpriseKey(enterprise.getEnterpriseName(), newEnterprise);
        }
        return rows;
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

    /**
     * 加载企业缓存数据
     */
    @Override
    public void loadingEnterpriseCache() {
        List<SysEnterprise> enterprisesList = enterpriseMapper.selectEnterpriseCacheList(new SysEnterprise());
        for (SysEnterprise enterprise : enterprisesList) {
            redisService.setCacheObject(getCacheKey(enterprise.getEnterpriseName()), enterprise);
        }
    }

    /**
     * 清空企业缓存数据
     */
    @Override
    public void clearEnterpriseCache() {
        Collection<String> keys = redisService.keys(Constants.SYS_ENTERPRISE_KEY + "*");
        redisService.deleteObject(keys);
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
     * 根据企业 key 新增|更新 cache
     *
     * @param newEnterprise 企业对象
     */
    @Override
    public void refreshEnterpriseCache(SysEnterprise newEnterprise) {
        redisService.setCacheObject(getCacheKey(newEnterprise.getEnterpriseName()), newEnterprise);
    }

    /**
     * 刷新指定企业 cache 的 key
     *
     * @param oldEnterpriseName 原企业账号
     * @param newEnterprise     企业对象
     */
    @Override
    public void refreshEnterpriseKey(String oldEnterpriseName, SysEnterprise newEnterprise) {
        redisService.deleteObject(delCacheKey(oldEnterpriseName));
        redisService.setCacheObject(getCacheKey(newEnterprise.getEnterpriseName()), newEnterprise);
    }

    /**
     * 设置cache key
     *
     * @param enterpriseName 企业账号
     * @return 缓存键key
     */
    private String getCacheKey(String enterpriseName) {
        return Constants.SYS_CONFIG_KEY + enterpriseName + ":" + enterpriseName;
    }

    /**
     * 删除cache key
     *
     * @param enterpriseName 企业账号
     * @return 缓存键key
     */
    private String delCacheKey(String enterpriseName) {
        return Constants.SYS_CONFIG_KEY + enterpriseName + ":" + enterpriseName;
    }
}