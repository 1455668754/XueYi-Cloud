package com.xueyi.system.api.domain.source;

import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class Source extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    private Long strategyId;

    /** 主写源 */
    String master;

    /** 主读源列表 */
    List<String> slave;

    /** 类型(N非主源 Y主源) */
    private String isMain;

    /** 策略源集合 */
    private List<Source> values;

    public Source() {

    }

    public Source(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<String> getSlave() {
        return slave;
    }

    public void setSlave(List<String> slave) {
        this.slave = slave;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public List<Source> getValues() {
        return values;
    }

    public void setValues(List<Source> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("strategyId", getStrategyId())
                .append("master", getMaster())
                .append("slave", getSlave())
                .append("isMain", getIsMain())
                .append("values", getValues())
                .toString();
    }
}