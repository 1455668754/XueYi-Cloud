package com.xueyi.tenant.api.tenant.domain.po;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;

/**
 * 数据源策略 持久化对象
 *
 * @author xueyi
 */
public class TeStrategyPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 数据源Id */
    @Excel(name = "数据源Id")
    @TableField("source_id")
    private Long sourceId;

    /** 数据源编码 */
    @Excel(name = "数据源编码")
    @TableField("source_slave")
    private String sourceSlave;

    /** 默认策略（Y是 N否） */
    @Excel(name = "默认策略", readConverterExp = "Y=是,N=否")
    @TableField("is_default")
    private String isDefault;

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceSlave(String sourceSlave) {
        this.sourceSlave = sourceSlave;
    }

    public String getSourceSlave() {
        return sourceSlave;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefault() {
        return isDefault;
    }

}