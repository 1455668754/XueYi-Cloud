package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import com.xueyi.common.security.utils.SecurityUtils;

/**
 * 角色-模块关联 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_role_module_merge")
public class SysRoleModuleMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    @TableField("role_id")
    private Long roleId;

    /** 模块Id */
    @TableField("module_id")
    private Long moduleId;

    public SysRoleModuleMerge() {
    }

    public SysRoleModuleMerge(Long roleId, Long moduleId) {
        setRoleId(roleId);
        setModuleId(moduleId);
        setEnterpriseId(SecurityUtils.getEnterpriseId());
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
