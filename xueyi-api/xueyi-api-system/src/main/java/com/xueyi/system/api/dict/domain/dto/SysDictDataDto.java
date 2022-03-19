package com.xueyi.system.api.dict.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 字典数据 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_dict_data", excludeProperty = {"name", "delFlag"})
public class SysDictDataDto extends SysDictDataPo {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("value", getValue())
                .append("label", getLabel())
                .append("sort", getSort())
                .append("isDefault", getIsDefault())
                .append("cssClass", getCssClass())
                .append("listClass", getListClass())
                .append("status", getStatus())
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