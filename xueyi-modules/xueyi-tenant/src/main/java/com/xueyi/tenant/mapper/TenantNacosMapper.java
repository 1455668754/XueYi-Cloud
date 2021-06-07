package com.xueyi.tenant.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.tenant.domain.TenantNacos;

/**
 * Nacos配置 数据层
 *
 * @author xueyi
 */
public interface TenantNacosMapper {
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
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertTenantNacos(TenantNacos tenantNacos);

    /**
     * 修改Nacos配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateTenantNacos(TenantNacos tenantNacos);

    /**
     * 修改Nacos配置排序
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateTenantNacosSort(TenantNacos tenantNacos);

    /**
     * 删除Nacos配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantNacosById(TenantNacos tenantNacos);

    /**
     * 批量删除Nacos配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenantNacos Nacos配置
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteTenantNacosByIds(TenantNacos tenantNacos);
}