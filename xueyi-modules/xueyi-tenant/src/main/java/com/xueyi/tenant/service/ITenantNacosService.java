package com.xueyi.tenant.service;

import java.util.List;
import com.xueyi.tenant.domain.TenantNacos;

/**
 * Nacos配置 业务层
 *
 * @author xueyi
 */
public interface ITenantNacosService {
    /**
     * 查询Nacos配置列表
     *
     * @param tenantNacos Nacos配置
     * @return Nacos配置集合
     */
    public List<TenantNacos> selectTenantNacosList(TenantNacos tenantNacos);


    /**
     * 查询Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return Nacos配置
     */
    public TenantNacos selectTenantNacosById(TenantNacos tenantNacos);

    /**
     * 新增Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    public int insertTenantNacos(TenantNacos tenantNacos);

    /**
     * 修改Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    public int updateTenantNacos(TenantNacos tenantNacos);

    /**
     * 修改Nacos配置排序
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    public int updateTenantNacosSort(TenantNacos tenantNacos);

    /**
     * 删除Nacos配置信息
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    public int deleteTenantNacosById(TenantNacos tenantNacos);

    /**
     * 批量删除Nacos配置
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    public int deleteTenantNacosByIds(TenantNacos tenantNacos);
}