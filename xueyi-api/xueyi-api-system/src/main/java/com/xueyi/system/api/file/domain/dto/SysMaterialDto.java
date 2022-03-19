package com.xueyi.system.api.file.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.file.domain.po.SysMaterialPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 素材 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "xy_material", excludeProperty = {"remark"})
public class SysMaterialDto extends SysMaterialPo {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id", getId())
                .append("folderId", getFolderId())
                .append("nick", getNick())
                .append("name", getName())
                .append("originalName", getOriginalName())
                .append("url", getUrl())
                .append("originalUrl", getOriginalUrl())
                .append("size", getSize())
                .append("type", getType())
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
