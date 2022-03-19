package com.xueyi.tenant.api.tenant.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户 数据传输对象
 *
 * @author xueyi
 */
@TableName("te_tenant")
public class TeTenantDto extends TeTenantPo {

    private static final long serialVersionUID = 1L;

    /** 策略信息 */
    @TableField(exist = false)
    private TeStrategyDto strategy;

    /** 权限Ids */
    @TableField(exist = false)
    private Long[] authIds;

    public static boolean isAdmin(String isLessor) {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

    public TeStrategyDto getStrategy() {
        return strategy;
    }

    public void setStrategy(TeStrategyDto strategy) {
        this.strategy = strategy;
    }

    public Long[] getAuthIds() {
        return authIds;
    }

    public void setAuthIds(Long[] authIds) {
        this.authIds = authIds;
    }

    public boolean isNotAdmin() {
        return !isAdmin(this.getIsLessor());
    }

    public boolean isAdmin() {
        return isAdmin(this.getIsLessor());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("strategyId", getStrategyId())
                .append("strategy", getStrategy())
                .append("name", getName())
                .append("systemName", getSystemName())
                .append("nick", getNick())
                .append("logo", getLogo())
                .append("nameFrequency", getNameFrequency())
                .append("isLessor", getIsLessor())
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
                .append("authIds", getAuthIds())
                .toString();
    }
}