package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import com.xueyi.common.security.utils.SecurityUtils;

/**
 * 租户-模块关联 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_tenant_module_merge")
public class SysTenantModuleMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 模块Id */
    @TableField("module_id")
    private Long moduleId;

    public SysTenantModuleMerge(){}

    public SysTenantModuleMerge(Long moduleId){
        setModuleId(moduleId);
        setEnterpriseId(SecurityUtils.getEnterpriseId());
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
