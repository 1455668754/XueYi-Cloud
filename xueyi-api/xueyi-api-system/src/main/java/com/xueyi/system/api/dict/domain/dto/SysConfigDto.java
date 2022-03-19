package com.xueyi.system.api.dict.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 参数配置 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_config", excludeProperty = {"status", "delFlag"})
public class SysConfigDto extends SysConfigPo {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("code", getCode())
                .append("value", getValue())
                .append("type", getType())
                .append("sort", getSort())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
