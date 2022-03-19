package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import com.xueyi.common.security.utils.SecurityUtils;

/**
 * 租户-菜单关联 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_tenant_menu_merge")
public class SysTenantMenuMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 菜单Id */
    @TableField("menu_id")
    private Long menuId;

    public SysTenantMenuMerge(){}

    public SysTenantMenuMerge(Long menuId){
        setMenuId(menuId);
        setEnterpriseId(SecurityUtils.getEnterpriseId());
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
