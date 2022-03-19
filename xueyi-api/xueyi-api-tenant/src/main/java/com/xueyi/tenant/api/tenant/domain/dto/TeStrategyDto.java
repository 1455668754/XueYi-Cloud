package com.xueyi.tenant.api.tenant.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.tenant.domain.po.TeStrategyPo;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源策略 数据传输对象
 *
 * @author xueyi
 */
@TableName("te_strategy")
public class TeStrategyDto extends TeStrategyPo {

    private static final long serialVersionUID = 1L;

    /** 数据源信息 */
    @TableField(exist = false)
    private TeSourceDto source;

    public TeSourceDto getSource() {
        return source;
    }

    public void setSource(TeSourceDto source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("sourceId", getSourceId())
            .append("source", getSource())
            .append("sourceSlave", getSourceSlave())
            .append("sort", getSort())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createName", getCreateName())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateName", getUpdateName())
            .append("updateTime", getUpdateTime())
            .append("isDefault", getIsDefault())
            .toString();
    }
}