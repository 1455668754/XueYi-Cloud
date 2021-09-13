package com.xueyi.tenant.domain;

import java.util.List;

import com.xueyi.tenant.api.domain.source.Source;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.web.domain.BaseEntity;

/**
 * 数据源策略对象 xy_tenant_strategy
 *
 * @author xueyi
 */
public class Strategy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    private Long strategyId;

    /** 策略名称 */
    private String name;

    /** 租户数量 */
    private String tenantAmount;

    /** 数据源数量 */
    private String sourceAmount;

    /** 是否有主数据源 */
    private Boolean hasMain;

    /** 是否可修改 */
    private Long isChange;

    /** 状态（0正常 1停用） */
    private String status;

    /** 数据源信息 */
    private List<Source> values;

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTenantAmount() {
        return tenantAmount;
    }

    public void setTenantAmount(String tenantAmount) {
        this.tenantAmount = tenantAmount;
    }

    public String getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(String sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public Boolean getHasMain() {
        return hasMain;
    }

    public void setHasMain(Boolean hasMain) {
        this.hasMain = hasMain;
    }

    public Long getIsChange() {
        return isChange;
    }

    public void setIsChange(Long isChange) {
        this.isChange = isChange;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public List<Source> getValues() {
        return values;
    }

    public void setValues(List<Source> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("strategyId", getStrategyId())
                .append("name", getName())
                .append("tenantAmount", getTenantAmount())
                .append("sourceAmount", getSourceAmount())
                .append("hasMain", getHasMain())
                .append("isChange", getIsChange())
                .append("values", getValues())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}