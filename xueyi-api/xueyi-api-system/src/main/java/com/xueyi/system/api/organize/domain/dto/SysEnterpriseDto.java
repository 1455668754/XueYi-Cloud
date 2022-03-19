package com.xueyi.system.api.organize.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 企业 数据传输对象
 *
 * @author xueyi
 */
@TableName("te_tenant")
public class SysEnterpriseDto extends SysEnterprisePo {

    private static final long serialVersionUID = 1L;

    public boolean isAdmin() {
        return isAdmin(getIsLessor());
    }

    public static boolean isAdmin(String isLessor) {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("systemName", getSystemName())
                .append("name", getName())
                .append("nick", getNick())
                .append("logo", getLogo())
                .append("isLessor", getIsLessor())
                .append("nameFrequency", getNameFrequency())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
