package com.xueyi.tenant.tenant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.feign.RemoteAuthService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.RemoteDeptService;
import com.xueyi.system.api.organize.feign.RemotePostService;
import com.xueyi.system.api.organize.feign.RemoteUserService;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import com.xueyi.tenant.tenant.manager.TeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeTenantServiceImpl extends BaseServiceImpl<TeTenantDto, TeTenantManager, TeTenantMapper> implements ITeTenantService {

    @Lazy
    @Autowired
    ITeTenantService oneselfService;

    @Autowired
    private RemoteAuthService remoteAuthService;

    @Autowired
    private ITeStrategyService strategyService;

    @Autowired
    private RemoteDeptService deptService;

    @Autowired
    private RemotePostService postService;

    @Autowired
    private RemoteUserService userService;

    @Autowired
    private RemoteAuthService authService;

    /**
     * 获取租户权限
     *
     * @param id id
     * @return 权限Ids
     */
    @Override
    public Long[] selectAuth(Long id) {
        TeTenantDto tenant = baseManager.selectById(id);
        TeStrategyDto strategy = strategyService.selectById(tenant.getStrategyId());
        R<Long[]> authR = remoteAuthService.getTenantAuthInner(tenant.getId(), strategy.getSourceSlave(), SecurityConstants.INNER);
        if (authR.isFail())
            throw new ServiceException(authR.getMessage());
        return authR.getResult();
    }

    /**
     * 修改租户权限
     *
     * @param id      id
     * @param authIds 权限Ids
     */
    @Override
    public void updateAuth(Long id, Long[] authIds) {
        TeTenantDto tenant = baseManager.selectById(id);
        if (tenant.isNotAdmin()) {
            TeStrategyDto strategy = strategyService.selectById(tenant.getStrategyId());
            R<Boolean> authR = remoteAuthService.editTenantAuthInner(authIds, tenant.getId(), strategy.getSourceSlave(), SecurityConstants.INNER);
            if (authR.isFail())
                throw new ServiceException(authR.getMessage());
        }
    }

    /**
     * 新增租户 | 包含数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    @Override
    @Transactional
    @GlobalTransactional
    public int insert(TeTenantRegister tenantRegister) {
        int rows = baseManager.insert(tenantRegister.getTenant());
        if (rows > 0) {
            TeStrategyDto strategy = strategyService.selectById(tenantRegister.getTenant().getStrategyId());
            tenantRegister.setSourceName(strategy.getSourceSlave());
            oneselfService.organizeInit(tenantRegister);
            if (tenantRegister.getTenant().isNotAdmin()) {
                oneselfService.authorityInit(tenantRegister);
            }
        }
        return rows;
    }

    /**
     * 校验源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果 | true/false 存在/不存在
     */
    @Override
    public boolean checkStrategyExist(Long strategyId) {
        return ObjectUtil.isNotNull(baseManager.checkStrategyExist(strategyId));
    }

    /**
     * 校验租户是否为默认租户
     *
     * @param id 租户id
     * @return 结果 | true/false 是/不是
     */
    @Override
    public boolean checkIsDefault(Long id) {
        TeTenantDto tenant = baseManager.selectById(id);
        return ObjectUtil.isNotNull(tenant) && StrUtil.equals(tenant.getIsDefault(), DictConstants.DicYesNo.YES.getCode());
    }

    /**
     * 租户组织数据初始化
     *
     * @param tenantRegister 租户初始化对象
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void organizeInit(TeTenantRegister tenantRegister) {
        Long enterpriseId = tenantRegister.getTenant().getId();
        String sourceName = tenantRegister.getSourceName();
        R<SysDeptDto> deptR = deptService.addInner(tenantRegister.getDept(), enterpriseId, sourceName, SecurityConstants.INNER);
        if (deptR.isFail())
            throw new ServiceException("新增失败，请检查！");
        tenantRegister.getPost().setDeptId(deptR.getResult().getId());
        R<SysPostDto> postR = postService.addInner(tenantRegister.getPost(), enterpriseId, sourceName, SecurityConstants.INNER);
        if (postR.isFail())
            throw new ServiceException("新增失败，请检查！");
        tenantRegister.getUser().setPostIds(new Long[]{postR.getResult().getId()});
        tenantRegister.getUser().setUserType(AuthorityConstants.UserType.ADMIN.getCode());
        tenantRegister.getUser().setPassword(SecurityUtils.encryptPassword(tenantRegister.getUser().getPassword()));
        R<SysUserDto> userR = userService.addInner(tenantRegister.getUser(), enterpriseId, sourceName, SecurityConstants.INNER);
        if (userR.isFail())
            throw new ServiceException("新增失败，请检查！");
    }

    /**
     * 租户权限数据初始化
     *
     * @param tenantRegister 租户初始化对象
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void authorityInit(TeTenantRegister tenantRegister) {
        R<Boolean> authR = authService.addTenantAuthInner(tenantRegister.getAuthIds(), tenantRegister.getTenant().getId(), tenantRegister.getSourceName(), SecurityConstants.INNER);
        if (authR.isFail())
            throw new ServiceException("新增失败，请检查！");
    }
}