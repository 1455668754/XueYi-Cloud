package com.xueyi.tenant.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.mapper.TenantCreationMapper;
import com.xueyi.tenant.service.ITenantCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 数据源 业务层处理
 *
 * @author xueyi
 */
@Service
public class TenantCreationServiceImpl implements ITenantCreationService {

    @Autowired
    private TenantCreationMapper tenantCreationMapper;

    /**
     * 租户新增-创建组织信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @DS("#tenant.sourceName")
    @Transactional(propagation = Propagation.REQUIRES_NEW)      // 事务传播特性设置为 REQUIRES_NEW 开启新的事务
    public int organizeCreation(Tenant tenant) {
        int rows = 0;
        Long deptId = IdUtil.getSnowflake(0, 0).nextId();
        Long postId = IdUtil.getSnowflake(0, 0).nextId();
        Long userId = IdUtil.getSnowflake(0, 0).nextId();
        if(tenant.getParams().containsKey("dept")){
            SysDept dept = (SysDept) tenant.getParams().get("dept");
            dept.setDeptId(deptId);
        }else{
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            tenant.getParams().put("dept",dept);
        }
        if(tenant.getParams().containsKey("post")){
            SysPost post = (SysPost) tenant.getParams().get("post");
            post.setDeptId(deptId);
            post.setPostId(postId);
        }else{
            SysPost post = new SysPost();
            post.setDeptId(deptId);
            post.setPostId(postId);
            tenant.getParams().put("post",post);
        }
        if(tenant.getParams().containsKey("user")){
            SysUser user = (SysUser) tenant.getParams().get("user");
            user.setDeptId(deptId);
            user.setPostId(postId);
            user.setUserId(userId);
        }else{
            SysUser user = new SysUser();
            user.setDeptId(deptId);
            user.setPostId(postId);
            user.setUserId(userId);
            tenant.getParams().put("user",user);
        }
        rows = rows + tenantCreationMapper.createDeptByTenantId(tenant);
        rows = rows + tenantCreationMapper.createPostByTenantId(tenant);
        rows = rows + tenantCreationMapper.createUserByTenantId(tenant);
        return rows;
    }
}