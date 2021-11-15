package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.mapper.TenantMapper;
import com.xueyi.tenant.service.ICreationService;
import com.xueyi.tenant.service.ITenantService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    private ICreationService creationService;

    /**
     * 查询租户信息列表
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<Tenant> mainSelectTenantList(Tenant tenant) {
        return tenantMapper.mainSelectTenantList(tenant);
    }

    /**
     * 查询租户信息
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public Tenant mainSelectTenantById(Tenant tenant) {
        return tenantMapper.mainSelectTenantById(tenant);
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
    public int mainInsertTenant(Tenant tenant) {
        /* 获取生成雪花Id，并赋值给主键，加入至子表对应外键中 */
        tenant.setTenantId(tenant.getSnowflakeId());
        String mainSourceName = DataSourceUtils.getMainSourceNameByStrategyId(tenant.getStrategyId());
        if (StringUtils.isNotEmpty(mainSourceName)) {
            tenant.setSourceName(mainSourceName);
            //新建租户时同步新建信息
            //1.新建租户的部门|岗位|超管用户信息
            creationService.organizeCreation(tenant);
            //1.新建租户的衍生角色&&模块|菜单屏蔽信息
            creationService.deriveRoleCreation(tenant);
            return tenantMapper.mainInsertTenant(tenant);
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
    public int mainRegisterTenant(Tenant tenant) {
        return tenantService.mainInsertTenant(tenant);
    }

    /**
     * 修改租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int mainUpdateTenant(Tenant tenant) {
        return tenantMapper.mainUpdateTenant(tenant);
    }

    /**
     * 修改租户信息排序
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int mainUpdateTenantSort(Tenant tenant) {
        return tenantMapper.mainUpdateTenantSort(tenant);
    }

    /**
     * 批量删除租户信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    public int mainDeleteTenantByIds(Tenant tenant) {
        return tenantMapper.mainDeleteTenantByIds(tenant);
    }

    /**
     * 查询租户Id存在于数组中的租户信息
     *
     * @param tenant 租户信息 | params.Ids 租户Ids组
     * @return 租户信息集合
     */
    public Set<Tenant> mainCheckTenantListByIds(Tenant tenant) {
        return tenantMapper.mainCheckTenantListByIds(tenant);
    }

    /**
     * 校验租户账号是否唯一
     *
     * @param tenant 租户信息 | tenantName 租户Id
     * @return 结果
     */
    @Override
    public String mainCheckTenantNameUnique(Tenant tenant) {
        Tenant info = tenantMapper.mainCheckTenantNameUnique(tenant);
        if (StringUtils.isNotNull(info)) {
            return BaseConstants.Check.NOT_UNIQUE.getCode();
        }
        return BaseConstants.Check.UNIQUE.getCode();
    }

    /**
     * 根据租户Id查询租户信息
     *
     * @param tenant 租户信息 | tenantId 租户Id
     * @return 租户信息
     */
    @Override
    public Tenant mainCheckTenantByTenantId(Tenant tenant) {
        return tenantMapper.mainCheckTenantByTenantId(tenant);
    }
}