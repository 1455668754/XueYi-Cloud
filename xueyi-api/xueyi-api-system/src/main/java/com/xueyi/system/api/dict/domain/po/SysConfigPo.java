package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.BaseEntity;

/**
 * 参数配置 持久化对象
 *
 * @author xueyi
 */
public class SysConfigPo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 参数键名 */
    @Excel(name = "参数键名")
    @TableField(value = "code", updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 参数键值 */
    @Excel(name = "参数键值")
    @TableField("value")
    private String value;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    @TableField(value = "type", updateStrategy = FieldStrategy.NEVER)
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
