package com.xueyi.tenant.controller;

import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.EnterpriseUtils;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.feign.RemoteConfigService;
import com.xueyi.system.api.feign.RemoteEnterpriseService;
import com.xueyi.tenant.api.model.TenantRegister;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.service.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 租户信息 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController {

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private RemoteConfigService remoteConfigService;

    @Autowired
    private RemoteEnterpriseService remoteEnterpriseService;

    /**
     * 查询租户信息列表
     */
    @RequiresPermissions("tenant:tenant:list")
    @GetMapping("/list")
    public AjaxResult list(Tenant tenant) {
        startPage();
        List<Tenant> list = tenantService.mainSelectTenantList(tenant);
        return getDataTable(list);
    }

    /**
     * 导出租户信息列表
     */
    @RequiresPermissions("tenant:tenant:export")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tenant tenant) throws IOException {
        List<Tenant> list = tenantService.mainSelectTenantList(tenant);
        ExcelUtil<Tenant> util = new ExcelUtil<Tenant>(Tenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @RequiresPermissions("tenant:tenant:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Tenant tenant) {
        return AjaxResult.success(tenantService.mainSelectTenantById(tenant));
    }

    /**
     * 新增租户信息
     */
    @RequiresPermissions("tenant:tenant:add")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tenant tenant) {
        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(new Tenant(tenant.getTenantName())))) {
            return AjaxResult.error("新增失败，该租户账号已存在，请修改后重试！");
        }
        return toAjax(addTenantCache(tenantService.mainInsertTenant(tenant), tenant));
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody TenantRegister register) {
        String key = remoteConfigService.getKey("sys.account.registerTenant").getData();
        if (!("true".equals(key))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        Tenant tenant = new Tenant(register.getEnterpriseName());
        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(tenant))) {
            return R.fail("注册租户'" + register.getEnterpriseSystemName() + "'失败，注册账号已存在");
        }
        //租户信息
        tenant.setStrategyId(TenantConstants.REGISTER_TENANT_STRATEGY_ID);
        tenant.setTenantName(register.getEnterpriseName());
        tenant.setTenantSystemName(register.getEnterpriseSystemName());
        tenant.setTenantNick(register.getEnterpriseNick());
        tenant.setTenantLogo(register.getLogo());

        //部门信息
        SysDept dept = new SysDept();
        dept.setDeptName(register.getNickName());
        tenant.getParams().put("dept", dept);
        //个人信息
        SysUser user = new SysUser();
        user.setUserName(register.getUserName());
        user.setNickName(register.getNickName());
        user.setEmail(register.getEmail());
        user.setPhone(register.getPhone());
        user.setSex(register.getSex());
        user.setAvatar(register.getAvatar());
        user.setProfile(register.getProfile());
        user.setPassword(register.getPassword());
        tenant.getParams().put("user", user);
        return R.ok(addTenantCache(tenantService.mainRegisterTenant(tenant), tenant) > 0);
    }

    /**
     * 修改租户信息
     */
    @RequiresPermissions("tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tenant tenant) {
        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(tenant))) {
            return AjaxResult.error("修改失败，该租户账号已存在，请修改后重试！");
        }
        Tenant check = tenantService.mainCheckTenantByTenantId(new Tenant(tenant.getTenantId()));
        if (StringUtils.equals(BaseConstants.Default.YES.getCode(), check.getIsChange())) {
            return AjaxResult.error("禁止操作系统租户");
        }
        return toAjax(refreshTenantCache(tenantService.mainUpdateTenant(tenant), check));
    }

    /**
     * 修改租户信息排序
     */
    @RequiresPermissions("tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Tenant tenant) {
        return toAjax(tenantService.mainUpdateTenantSort(tenant));
    }

    /**
     * 删除租户信息
     */
    @RequiresPermissions("tenant:tenant:remove")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Tenant tenant) {
        Set<Tenant> before = tenantService.mainCheckTenantListByIds(tenant);
        int rows = tenantService.mainDeleteTenantByIds(tenant);
        if (rows > 0) {
            Set<Tenant> after = tenantService.mainCheckTenantListByIds(tenant);
            before.removeAll(after);
            for (Tenant vo : before) {
                EnterpriseUtils.deleteEnterpriseAllCache(vo.getTenantId(), vo.getTenantName());
            }
        }
        return toAjax(rows);
    }

    /**
     * 新增租户 cache
     *
     * @param rows   结果
     * @param tenant 租户信息
     * @return 结果
     */
    private int addTenantCache(int rows, Tenant tenant) {
        if (rows > 0 && !StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), tenant.getStatus())) {
            remoteEnterpriseService.refreshEnterpriseAllCache(tenant.getTenantId(), SecurityConstants.INNER);
        }
        return rows;
    }

    /**
     * 修改租户 cache
     *
     * @param rows      结果
     * @param oldTenant 原始租户信息
     * @return 结果
     */
    private int refreshTenantCache(int rows, Tenant oldTenant) {
        if (rows > 0) {
            R<SysEnterprise> enterprise = remoteEnterpriseService.getEnterpriseByEnterpriseId(oldTenant.getTenantId(), SecurityConstants.INNER);
            if (R.FAIL == enterprise.getCode()) {
                throw new ServiceException(enterprise.getMsg());
            }
            if (!StringUtils.equals(enterprise.getData().getStatus(), oldTenant.getStatus())) {
                if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), enterprise.getData().getStatus())) {
                    remoteEnterpriseService.refreshEnterpriseAllCache(oldTenant.getTenantId(), SecurityConstants.INNER);
                } else {
                    EnterpriseUtils.deleteEnterpriseAllCache(oldTenant.getTenantId(), oldTenant.getTenantName());
                }
            } else {
                if (!StringUtils.equals(enterprise.getData().getEnterpriseName(), oldTenant.getTenantName())) {
                    EnterpriseUtils.deleteLoginCache(oldTenant.getTenantName());
                    EnterpriseUtils.refreshLoginCache(enterprise.getData().getEnterpriseName(), enterprise.getData().getEnterpriseId());
                }
                EnterpriseUtils.refreshEnterpriseCache(enterprise.getData().getEnterpriseId(), enterprise.getData());
            }
        }
        return rows;
    }
}