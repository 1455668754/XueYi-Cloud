package com.xueyi.tenant.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.tenant.mapper.TenantNacosMapper;
import com.xueyi.tenant.domain.TenantNacos;
import com.xueyi.tenant.service.ITenantNacosService;

/**
 * Nacos配置 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantNacosServiceImpl implements ITenantNacosService {
    @Autowired
    private TenantNacosMapper tenantNacosMapper;

    /**
     * 查询Nacos配置列表
     *
     * @param tenantNacos Nacos配置
     * @return Nacos配置
     */
    @Override
    public List<TenantNacos> selectTenantNacosList(TenantNacos tenantNacos) {
        return tenantNacosMapper.selectTenantNacosList(tenantNacos);
    }

    /**
     * 查询Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return Nacos配置
     */
    @Override
    public TenantNacos selectTenantNacosById(TenantNacos tenantNacos) {
        return tenantNacosMapper.selectTenantNacosById(tenantNacos);
    }

    /**
     * 新增Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @Override
    public int insertTenantNacos(TenantNacos tenantNacos) {
        return tenantNacosMapper.insertTenantNacos(tenantNacos);
    }

    /**
     * 修改Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @Override
    public int updateTenantNacos(TenantNacos tenantNacos) {
        return tenantNacosMapper.updateTenantNacos(tenantNacos);
    }

    /**
     * 修改Nacos配置排序
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @Override
    public int updateTenantNacosSort(TenantNacos tenantNacos) {
        return tenantNacosMapper.updateTenantNacosSort(tenantNacos);
    }

    /**
     * 删除Nacos配置信息
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @Override
    public int deleteTenantNacosById(TenantNacos tenantNacos) {
        return tenantNacosMapper.deleteTenantNacosById(tenantNacos);
    }

    /**
     * 批量删除Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @Override
    public int deleteTenantNacosByIds(TenantNacos tenantNacos) {
        return tenantNacosMapper.deleteTenantNacosByIds(tenantNacos);
    }
}