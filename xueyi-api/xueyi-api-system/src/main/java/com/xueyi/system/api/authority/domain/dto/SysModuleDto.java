package com.xueyi.system.api.authority.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 模块 数据传输对象
 *
 * @author xueyi
 */
@TableName("sys_module")
public class SysModuleDto extends SysModulePo<SysMenuDto> {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("logo", getLogo())
                .append("path", getPath())
                .append("paramPath", getParamPath())
                .append("type", getType())
                .append("hideModule", getHideModule())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("isCommon", getIsCommon())
                .append("isDefault", getIsDefault())
                .append("subList", getSubList())
                .toString();
    }
}
