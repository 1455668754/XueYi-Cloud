package com.xueyi.tenant.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.api.feign.RemoteConfigService;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.tenant.api.model.TenantRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.tenant.domain.Tenant;
import com.xueyi.tenant.service.ITenantService;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.page.TableDataInfo;

/**
 * 租户信息 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController
{
    @Autowired
    private ITenantService tenantService;

    @Autowired
    private RemoteConfigService remoteConfigService;

    /**
     * 查询租户信息列表
     */
    @PreAuthorize(hasPermi = "tenant:tenant:list")
    @GetMapping("/list")
    public TableDataInfo list(Tenant tenant)
    {
        startPage();
        List<Tenant> list = tenantService.mainSelectTenantList(tenant);
        return getDataTable(list);
    }

    /**
     * 导出租户信息列表
     */
    @PreAuthorize(hasPermi = "tenant:tenant:export")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tenant tenant) throws IOException
    {
        List<Tenant> list = tenantService.mainSelectTenantList(tenant);
        ExcelUtil<Tenant> util = new ExcelUtil<Tenant>(Tenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:query")
    @GetMapping(value = "/byId")
    public AjaxResult getInfo(Tenant tenant)
    {
        return AjaxResult.success(tenantService.mainSelectTenantById(tenant));
    }

    /**
     * 新增租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:add")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tenant tenant)
    {
        Tenant check = new Tenant(tenant.getTenantName());
        if (UserConstants.NOT_UNIQUE.equals(tenantService.mainCheckTenantNameUnique(check))) {
            return AjaxResult.error("新增失败，该租户账号已存在，请修改后重试！");
        }
        int rows = tenantService.mainInsertTenant(tenant);
        if (rows > 0) {
            tenantService.refreshTenantCache(tenant.getTenantId());
        }
        return toAjax(rows);
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody TenantRegister register)
    {
        String key = remoteConfigService.getKey("sys.account.registerTenant").getData();
        if (!("true".equals(key)))
        {
            return R.fail("当前系统没有开启注册功能！");
        }
        Tenant tenant = new Tenant(register.getEnterpriseName());
        if (UserConstants.NOT_UNIQUE.equals(tenantService.mainCheckTenantNameUnique(tenant))) {
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
        tenant.getParams().put("dept",dept);
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
        tenant.getParams().put("user",user);
        return R.ok(tenantService.mainRegisterTenant(tenant));
    }

    /**
     * 修改租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tenant tenant)
    {
        if(tenant.getIsChange().equals("Y")){
            return AjaxResult.error("禁止操作系统租户");
        }
        return toAjax(tenantService.mainUpdateTenant(tenant));
    }

    /**
     * 修改租户信息排序
     */
    @PreAuthorize(hasPermi = "tenant:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/sort")
    public AjaxResult updateSort(@RequestBody Tenant tenant)
    {
        return toAjax(tenantService.mainUpdateTenantSort(tenant));
    }

    /**
     * 删除租户信息
     */
    @PreAuthorize(hasPermi = "tenant:tenant:remove")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody Tenant tenant)
    {
        return toAjax(tenantService.mainDeleteTenantByIds(tenant));
    }
}