package com.xueyi.system.api.domain.organize;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户表 xy_tenant
 *
 * @author xueyi
 */
public class SysEnterprise extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    private Long strategyId;

    /** 企业账号 */
    private String enterpriseName;

    /** 系统名称 */
    private String enterpriseSystemName;

    /** 企业昵称 */
    private String enterpriseNick;

    /** 企业logo */
    private String logo;

    /** 超管租户（Y是 N否） */
    private String isLessor;

    /** 企业账号修改次数 */
    private Long enterpriseNameFrequency;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseSystemName() {
        return enterpriseSystemName;
    }

    public void setEnterpriseSystemName(String enterpriseSystemName) {
        this.enterpriseSystemName = enterpriseSystemName;
    }

    public String getEnterpriseNick() {
        return enterpriseNick;
    }

    public void setEnterpriseNick(String enterpriseNick) {
        this.enterpriseNick = enterpriseNick;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public Long getEnterpriseNameFrequency() {
        return enterpriseNameFrequency;
    }

    public void setEnterpriseNameFrequency(Long enterpriseNameFrequency) {
        this.enterpriseNameFrequency = enterpriseNameFrequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin(this.isLessor);
    }

    public static boolean isAdmin(String isLessor) {
        return StringUtils.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("enterpriseId", getEnterpriseId())
                .append("enterpriseSystemName", getEnterpriseSystemName())
                .append("enterpriseName", getEnterpriseName())
                .append("enterpriseNick", getEnterpriseNick())
                .append("logo", getLogo())
                .append("isLessor", getIsLessor())
                .append("enterpriseNameFrequency", getEnterpriseNameFrequency())
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