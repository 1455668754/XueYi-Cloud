package com.xueyi.system.tenant.service.impl;

import cn.hutool.core.util.IdUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.tenant.domain.SysTenant;
import com.xueyi.system.tenant.mapper.SysTenantMapper;
import com.xueyi.system.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService {
    @Autowired
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询租户管理
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public SysTenant selectSysTenantById(SysTenant sysTenant) {
        return sysTenantMapper.selectSysTenantById(sysTenant);
    }

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant) {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户管理
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertSysTenant(SysTenant sysTenant) {
        int row = sysTenantMapper.insertSysTenant(sysTenant);
        row = row + sysTenantMapper.insertSysTenantDatabase(sysTenant);
        sysTenant.setTenantId(sysTenant.getId());
        sysTenant.getParams().put("deptId", IdUtil.getSnowflake(0, 0).nextId());
        sysTenant.getParams().put("postId", IdUtil.getSnowflake(0, 0).nextId());
        sysTenant.getParams().put("userId", IdUtil.getSnowflake(0, 0).nextId());
        sysTenantMapper.createDeptByTenantId(sysTenant);
        sysTenantMapper.createPostByTenantId(sysTenant);
        sysTenantMapper.createUserByTenantId(sysTenant);
        return row;
    }

    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysTenant(SysTenant sysTenant) {
        int row = sysTenantMapper.updateSysTenant(sysTenant);
        row = row + sysTenantMapper.updateSysTenantDatabase(sysTenant);
        return row;
    }

    /**
     * 删除租户管理信息
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysTenantById(SysTenant sysTenant) {
        int row = sysTenantMapper.deleteSysTenantById(sysTenant);
        row = row + sysTenantMapper.deleteSysTenantDatabaseById(sysTenant);
        return row;
    }


}