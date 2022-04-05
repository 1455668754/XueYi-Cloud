package com.xueyi.system.api.source.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 源策略 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "te_strategy", excludeProperty = {"name", "sort", "remark", "createBy", "createTime", "updateBy", "updateTime"})
public class Source extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主写源 */
    @TableField("source_slave")
    String master;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("master", getMaster())
                .toString();
    }
}