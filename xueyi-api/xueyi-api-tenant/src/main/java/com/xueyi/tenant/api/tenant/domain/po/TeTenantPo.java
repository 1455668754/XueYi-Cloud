package com.xueyi.tenant.api.tenant.domain.po;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.xss.Xss;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 租户 持久化对象
 *
 * @author xueyi
 */
public class TeTenantPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    @Excel(name = "策略Id")
    @TableField("strategy_id")
    private Long strategyId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    @TableField("system_name")
    private String systemName;

    /** 租户名称 */
    @Excel(name = "租户名称")
    @TableField("nick")
    private String nick;

    /** 租户logo */
    @Excel(name = "租户logo")
    @TableField("logo")
    private String logo;

    /** 账号修改次数 */
    @Excel(name = "账号修改次数")
    @TableField("name_frequency")
    private Integer nameFrequency;

    /** 超管租户（Y是 N否） */
    @Excel(name = "超管租户", readConverterExp = "Y=是,N=否")
    @TableField("is_lessor")
    private String isLessor;

    /** 默认租户（Y是 N否） */
    @Excel(name = "默认租户", readConverterExp = "Y=是,N=否")
    @TableField("is_default")
    private String isDefault;

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    @Override
    @Xss(message = "企业账号不能包含脚本字符")
    @NotBlank(message = "企业账号不能为空")
    @Size(max = 30, message = "企业账号长度不能超过30个字符")
    public String getName() {
        return super.getName();
    }

    @Xss(message = "系统名称不能包含脚本字符")
    @NotBlank(message = "系统名称不能为空")
    @Size(max = 30, message = "系统名称长度不能超过30个字符")
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @Xss(message = "企业名称不能包含脚本字符")
    @NotBlank(message = "企业名称不能为空")
    @Size(max = 30, message = "企业名称长度不能超过30个字符")
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setNameFrequency(Integer nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    public Integer getNameFrequency() {
        return nameFrequency;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefault() {
        return isDefault;
    }

}