package com.xueyi.tenant.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;

/**
 * 租户信息对象 xy_tenant
 *
 * @author xueyi
 */
public class Tenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    private Long tenantId;

    /** 策略Id */
    private Long strategyId;

    /** 租户账号 */
    @Excel(name = "租户账号")
    private String tenantName;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String tenantSystemName;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantNick;

    /** 租户logo */
    @Excel(name = "租户logo")
    private String tenantLogo;

    /** 系统账户（Y是 N否） */
    private String isChange;

    /** 租户账号修改次数 */
    @Excel(name = "租户账号修改次数")
    private Long tenantNameFrequency;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 策略信息 */
    private Strategy strategy;

    /** 系统-菜单组（菜单权限） */
    private Long[] systemMenuIds;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantSystemName(String tenantSystemName) {
        this.tenantSystemName = tenantSystemName;
    }

    public String getTenantSystemName() {
        return tenantSystemName;
    }

    public void setTenantNick(String tenantNick) {
        this.tenantNick = tenantNick;
    }

    public String getTenantNick() {
        return tenantNick;
    }

    public void setTenantLogo(String tenantLogo) {
        this.tenantLogo = tenantLogo;
    }

    public String getTenantLogo() {
        return tenantLogo;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public void setTenantNameFrequency(Long tenantNameFrequency) {
        this.tenantNameFrequency = tenantNameFrequency;
    }

    public Long getTenantNameFrequency() {
        return tenantNameFrequency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Long[] getSystemMenuIds() {
        return systemMenuIds;
    }

    public void setSystemMenuIds(Long[] systemMenuIds) {
        this.systemMenuIds = systemMenuIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("strategyId",getStrategyId())
                .append("tenantName", getTenantName())
                .append("tenantSystemName", getTenantSystemName())
                .append("tenantNick", getTenantNick())
                .append("tenantLogo", getTenantLogo())
                .append("isChange", getIsChange())
                .append("tenantNameFrequency", getTenantNameFrequency())
                .append("strategy", getStrategy())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}