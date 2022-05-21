package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;

/**
 * 角色-菜单关联 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_role_menu_merge")
public class SysRoleMenuMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    @TableField("role_id")
    private Long roleId;

    /** 菜单Id */
    @TableField("menu_id")
    private Long menuId;

    public SysRoleMenuMerge() {
    }

    public SysRoleMenuMerge(Long roleId, Long menuId) {
        setRoleId(roleId);
        setMenuId(menuId);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
