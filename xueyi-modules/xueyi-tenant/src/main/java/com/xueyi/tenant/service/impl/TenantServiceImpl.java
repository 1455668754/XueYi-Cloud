package com.xueyi.tenant.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.feign.RemoteConfigService;
import com.xueyi.system.api.feign.RemoteEnterpriseService;
import com.xueyi.tenant.api.domain.source.TenantSource;
import com.xueyi.tenant.domain.TenantStrategy;
import com.xueyi.tenant.mapper.TenantStrategyMapper;
import com.xueyi.tenant.service.ITenantCreationService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.datascope.annotation.DataScope;
import org.springframework.transaction.annotation.Transactional;
import com.xueyi.tenant.mapper.TenantMapper;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.service.ITenantService;

/**
 * 租户信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TenantServiceImpl implements ITenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private TenantStrategyMapper tenantStrategyMapper;

    @Autowired
    private ITenantCreationService tenantCreationService;

    @Autowired
    private RemoteEnterpriseService remoteEnterpriseService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询租户信息列表
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<Tenant> selectTenantList(Tenant tenant) {
        return tenantMapper.selectTenantList(tenant);
    }

    /**
     * 查询租户信息
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public Tenant selectTenantById(Tenant tenant) {
        return tenantMapper.selectTenantById(tenant);
    }

    /**
     * 新增租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    @GlobalTransactional
    @DataScope(ueAlias = "empty")
    public int insertTenant(Tenant tenant) {
        /* 获取生成雪花Id，并赋值给主键，加入至子表对应外键中 */
        tenant.setTenantId(tenant.getSnowflakeId());
        TenantStrategy search = new TenantStrategy();
        search.setStrategyId(tenant.getStrategyId());
        TenantStrategy strategy = tenantStrategyMapper.selectTenantStrategyById(search);
        for (TenantSource source : strategy.getValues()) {
            if (source.getIsMain().equals("Y")) {
                tenant.setSourceName(source.getSlave());
                //新建租户时同步新建信息
                //1.新建租户的部门|岗位|超管用户信息
                tenantCreationService.organizeCreation(tenant);
                //1.新建租户的衍生角色&&模块|菜单屏蔽信息
                tenantCreationService.deriveRoleCreation(tenant);
                int rows = tenantMapper.insertTenant(tenant);
                if (rows > 0) {
                    refreshCache(tenant.getTenantId());
                }
                return rows;
            }
        }
        return 0;
    }

    /**
     * 注册租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public Boolean registerTenant(Tenant tenant) {
        int row = tenantService.insertTenant(tenant);
        return row > 0;
    }

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int updateTenant(Tenant tenant) {
        int rows = tenantMapper.updateTenant(tenant);
        if (rows > 0) {
            deleteCache(tenant.getTenantId());
            refreshCache(tenant.getTenantId());
        }
        return rows;
    }

    /**
     * 修改租户信息排序
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int updateTenantSort(Tenant tenant) {
        return tenantMapper.updateTenantSort(tenant);
    }

    /**
     * 删除租户信息信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int deleteTenantById(Tenant tenant) {
        int rows = tenantMapper.deleteTenantById(tenant);
        if (rows > 0) {
            deleteCache(tenant.getTenantId());
            deleteCacheFolder(tenant.getTenantId());
        }
        return rows;
    }

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int deleteTenantByIds(Tenant tenant) {
        List<Long> Ids = (List<Long>) tenant.getParams().get("Ids");
        int rows = 0;
        Tenant delTenant = new Tenant();
        for (Long Id : Ids) {
            delTenant.setTenantId(Id);
            rows += deleteTenantById(delTenant);
        }
        return rows;
    }

    /**
     * 校验租户账号是否唯一
     *
     * @param tenant 租户信息 | tenantName 租户Id
     * @return 结果
     */
    @Override
    public String checkTenantNameUnique(Tenant tenant) {
        Tenant info = tenantMapper.checkTenantNameUnique(tenant);
        if (StringUtils.isNotNull(info)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增指定租户Id cache
     *
     * @param tenantId 租户Id
     */
    private void refreshCache(Long tenantId) {
        R<SysEnterprise> enterprise = remoteEnterpriseService.getEnterpriseByEnterpriseId(tenantId, SecurityConstants.INNER);
        if (R.FAIL == enterprise.getCode()) {
            throw new ServiceException(enterprise.getMsg());
        }
        if (StringUtils.equals(enterprise.getData().getStatus(), TenantConstants.NORMAL)) {
            redisService.setCacheObject(EnterpriseUtils.getEnterpriseCacheKey(tenantId), enterprise);
            redisService.setCacheObject(EnterpriseUtils.getLoginCacheKey(enterprise.getData().getEnterpriseName()), enterprise.getData().getEnterpriseId());
        }
    }

    /**
     * 删除指定租户Id cache
     *
     * @param tenantId 租户Id
     */
    private void deleteCache(Long tenantId) {
        R<SysEnterprise> enterprise = remoteEnterpriseService.getEnterpriseByEnterpriseId(tenantId, SecurityConstants.INNER);
        if (R.FAIL == enterprise.getCode()) {
            throw new ServiceException(enterprise.getMsg());
        }
        redisService.deleteObject(EnterpriseUtils.getEnterpriseCacheKey(tenantId));
        redisService.deleteObject(EnterpriseUtils.getLoginCacheKey(enterprise.getData().getEnterpriseName()));
    }

    /**
     * 删除指定租户Id cache目录
     *
     * @param tenantId 租户Id
     */
    private void deleteCacheFolder(Long tenantId) {
        redisService.deleteObject(EnterpriseUtils.getCacheFolderKey(tenantId));
    }
}