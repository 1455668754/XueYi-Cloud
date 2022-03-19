package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;

/**
 * 角色管理 服务层
 *
 * @author xueyi
 */
public interface ISysRoleService extends IBaseService<SysRoleDto> {

    /**
     * 修改角色组织权限
     *
     * @param role 角色对象
     * @return 结果
     */
    int updateDataScope(SysRoleDto role);

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkRoleCodeUnique(Long Id, String code);

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkRoleKeyUnique(Long Id, String roleKey);
}
