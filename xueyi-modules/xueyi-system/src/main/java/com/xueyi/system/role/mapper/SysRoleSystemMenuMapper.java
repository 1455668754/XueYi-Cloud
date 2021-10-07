package com.xueyi.system.role.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;

/**
 * 角色和系统-菜单关联Mapper接口
 *
 * @author xueyi
 */
public interface SysRoleSystemMenuMapper {

    /**
     * 查询系统-菜单使用数量
     * 访问控制 rsm 租户查询
     *
     * @param search 万用组件 | systemMenuId 系统-菜单Id
     * @return 结果
     */
    @DataScope(eAlias = "rsm")
    public int checkSystemMenuExistRole(SysSearch search);
}
