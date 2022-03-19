package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;

/**
 * 字典类型 持久化对象
 *
 * @param <S> SubDto
 * @author xueyi
 */
public class SysDictTypePo<S> extends SubBaseEntity<S> {

    private static final long serialVersionUID = 1L;

    /** 字典类型 */
    @Excel(name = "字典类型")
    @TableField(value = "code", updateStrategy = FieldStrategy.NEVER)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
