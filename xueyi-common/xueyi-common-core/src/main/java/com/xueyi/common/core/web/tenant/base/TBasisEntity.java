package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.BasisEntity;

/**
 * Basis 租户基类
 *
 * @author xueyi
 */
public class TBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(exist = false)
    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
