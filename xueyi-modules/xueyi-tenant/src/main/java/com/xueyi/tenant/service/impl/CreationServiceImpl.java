package com.xueyi.tenant.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.RoleConstants;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.mapper.CreationMapper;
import com.xueyi.tenant.service.ICreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class CreationServiceImpl implements ICreationService {

    @Autowired
    private CreationMapper creationMapper;

    /**
     * 租户新增-创建组织信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @DS("#tenant.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int organizeCreation(Tenant tenant) {
        int d, p, u;
        Long deptId = IdUtil.getSnowflake(0, 0).nextId();
        Long postId = IdUtil.getSnowflake(0, 0).nextId();
        Long userId = IdUtil.getSnowflake(0, 0).nextId();
        // 1.建立新租户初始部门信息
        SysDept dept = tenant.getParams().containsKey("dept") ? (SysDept) tenant.getParams().get("dept") : new SysDept(deptId);
        if (!tenant.getParams().containsKey("dept")) {
            tenant.getParams().put("dept", dept);
        }
        // 2.建立新租户初始岗位信息
        SysPost post = tenant.getParams().containsKey("post") ? (SysPost) tenant.getParams().get("post") : new SysPost(postId, deptId);
        if (!tenant.getParams().containsKey("post")) {
            tenant.getParams().put("post", post);
        }
        // 3.建立新租户初始用户信息
        SysUser user = tenant.getParams().containsKey("user") ? (SysUser) tenant.getParams().get("user") : new SysUser(userId, postId, deptId);
        if (!tenant.getParams().containsKey("user")) {
            tenant.getParams().put("user", user);
        }
        d = creationMapper.createDeptByTenantId(tenant);
        p = creationMapper.createPostByTenantId(tenant);
        u = creationMapper.createUserByTenantId(tenant);
        return d + p + u;
    }

    /**
     * 租户新增-创建衍生角色 && 屏蔽指定模块&&菜单
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @DS("#tenant.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deriveRoleCreation(Tenant tenant) {
        int r, or;
        Long deriveAdministratorId = IdUtil.getSnowflake(0, 0).nextId();
        Long deriveTenantId = IdUtil.getSnowflake(0, 0).nextId();
        // 1.建立新租户初始租管衍生角色信息
        SysRole deriveAdministrator = tenant.getParams().containsKey("role") ? (SysRole) tenant.getParams().get("role") : new SysRole(deriveAdministratorId);
        deriveAdministrator.setType(RoleConstants.ADMINISTRATOR_DERIVE_TYPE);
        deriveAdministrator.setName("超管衍生" + deriveAdministratorId);
        if (!tenant.getParams().containsKey("role")) {
            tenant.getParams().put("role", deriveAdministrator);
        }
        // 2.建立新租户初始租户衍生角色信息
        SysRole deriveTenant = new SysRole(deriveTenantId);
        deriveTenant.setType(RoleConstants.ENTERPRISE_DERIVE_TYPE);
        deriveTenant.setName("租户衍生" + deriveTenantId);
        tenant.getParams().put("deriveRole", deriveTenant);
        r = creationMapper.createRoleByTenantId(tenant);
        or = creationMapper.createOrganizeRoleByTenantId(tenant);
        return r + or;
    }
}