package com.xueyi.tenant.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;

/**
 * 数据源策略对象 xy_tenant_strategy
 *
 * @author xueyi
 */
public class TenantStrategy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略Id */
    private Long strategyId;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String name;

    /** 数据源数量 */
    @Excel(name = "数据源数量")
    private String amount;

    /** 是否有主数据源 */
    private Boolean hasMain;

    /** 是否可修改 */
    private Long isChange;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 数据源信息 */
    private List<TenantSource> values;

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

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
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

    public List<TenantSource> getValues() {
        return values;
    }

    public void setValues(List<TenantSource> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("strategyId", getStrategyId())
                .append("name", getName())
                .append("amount", getAmount())
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