package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.TreeEntity;

import static com.xueyi.common.core.constant.basic.TenantConstants.TENANT_ID;

/**
 * Tree 租户基类
 *
 * @param <D> Dto
 * @author xueyi
 */
public class TTreeEntity<D> extends TreeEntity<D> {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(value = TENANT_ID, updateStrategy = FieldStrategy.NEVER, select = false)
    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
