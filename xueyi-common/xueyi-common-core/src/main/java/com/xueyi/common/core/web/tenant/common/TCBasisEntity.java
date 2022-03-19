package com.xueyi.common.core.web.tenant.common;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.common.CBasisEntity;

import static com.xueyi.common.core.constant.basic.TenantConstants.TENANT_ID;

/**
 * Basis 租户混合基类
 *
 * @author xueyi
 */
public class TCBasisEntity extends CBasisEntity {

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
