package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.authority.manager.SysRoleManager;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysAuthService;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 角色管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDto, SysRoleManager, SysRoleMapper> implements ISysRoleService {

    @Autowired
    private ISysAuthService authService;

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 查询角色对象列表 | 数据权限 | 附加数据
     *
     * @param role 角色对象
     * @return 角色对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysRoleMapper"})
    public List<SysRoleDto> selectListScope(SysRoleDto role) {
        return baseManager.selectListExtra(role);
    }

    /**
     * 新增角色对象
     *
     * @param role 角色对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysRoleDto role) {
        int row = baseManager.insert(role);
        if (row > 0) {
            authService.addRoleAuth(role.getId(), role.getAuthIds());
            organizeService.addRoleOrganizeMerge(role.getId(),
                    StrUtil.equals(role.getDataScope(), AuthorityConstants.DataScope.CUSTOM.getCode())
                            ? role.getOrganizeIds()
                            : new Long[]{});
        }
        return row;
    }

    /**
     * 修改角色组织权限
     *
     * @param role 角色对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int updateDataScope(SysRoleDto role) {
        int row = baseManager.updateDataScope(role.getId(), role.getRoleKey(), role.getDataScope());
        if (row > 0) {
            organizeService.editRoleOrganizeMerge(role.getId(),
                    StrUtil.equals(role.getDataScope(), AuthorityConstants.DataScope.CUSTOM.getCode())
                            ? role.getOrganizeIds()
                            : new Long[]{});
        }
        return row;
    }

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkRoleCodeUnique(Long Id, String code) {
        return ObjectUtil.isNotNull(baseManager.checkRoleCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, code));
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkRoleKeyUnique(Long Id, String roleKey) {
        return ObjectUtil.isNotNull(baseManager.checkRoleKeyUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, roleKey));
    }

}
