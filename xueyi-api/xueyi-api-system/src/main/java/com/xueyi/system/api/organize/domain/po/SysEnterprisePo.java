package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.xss.Xss;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 企业 持久化对象
 *
 * @author xueyi
 */
public class SysEnterprisePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    @TableField("strategy_id")
    private Long strategyId;

    /** 系统名称 */
    @TableField("system_name")
    private String systemName;

    /** 企业名称 */
    @TableField("nick")
    private String nick;

    /** 企业logo */
    @TableField("logo")
    private String logo;

    /** 超管租户（Y是 N否） */
    @TableField("is_lessor")
    private String isLessor;

    /** 企业账号修改次数 */
    @TableField("name_frequency")
    private Long nameFrequency;

    /** 默认企业（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

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

    public Long getNameFrequency() {
        return nameFrequency;
    }

    public void setNameFrequency(Long nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
