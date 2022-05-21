package com.xueyi.common.core.web.tenant.common;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.common.CBaseEntity;

import static com.xueyi.common.core.constant.basic.TenantConstants.TENANT_ID;

/**
 * Base 租户混合基类
 *
 * @author xueyi
 */
public class TCBaseEntity extends CBaseEntity {

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
