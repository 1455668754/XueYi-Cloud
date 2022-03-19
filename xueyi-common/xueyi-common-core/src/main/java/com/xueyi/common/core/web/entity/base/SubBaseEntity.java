package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

/**
 * SubBase 基类
 *
 * @param <S> SubDto
 * @author xueyi
 */
public class SubBaseEntity<S> extends BaseEntity {

    /** 子数据集合 */
    @TableField(exist = false)
    private List<S> subList;

    public List<S> getSubList() {
        return subList;
    }

    public void setSubList(List<S> subList) {
        this.subList = subList;
    }
}
