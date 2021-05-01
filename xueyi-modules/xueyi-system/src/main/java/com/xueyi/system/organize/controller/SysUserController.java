package com.xueyi.system.organize.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.organize.SysDept;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.model.LoginUser;

/**
 * 用户信息
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysLoginService loginService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info/{enterpriseName}/{userName}")
    public R<LoginUser> info(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName) {
        SysEnterprise sysEnterprise = loginService.checkLoginByEnterpriseName(enterpriseName);
        if (StringUtils.isNull(sysEnterprise)) {
            return R.fail("账号或密码错误，请检查");
        }
        SysUser sysUser = loginService.checkLoginByEnterpriseIdANDUserName(sysEnterprise.getEnterpriseId(), userName);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("账号或密码错误，请检查");
        }
        // 角色集合
        Set<String> roles = loginService.getRolePermission(sysEnterprise.getEnterpriseId(), sysUser.getDeptId(), sysUser.getPostId(), sysUser.getUserId(), sysUser.getUserType());
        // 权限集合
        Set<String> permissions = loginService.getMenuPermission(sysEnterprise.getEnterpriseId(), sysUser.getUserId(), sysUser.getUserType());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setUserType(sysUser.getUserType());
        sysUserVo.setSysEnterprise(sysEnterprise);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser();
        // 角色集合
        Set<String> roles = loginService.getRolePermission(loginUser.getEnterpriseId(), loginUser.getSysUser().getDeptId(), loginUser.getSysUser().getPostId(), loginUser.getSysUser().getUserId(), loginUser.getSysUser().getUserType());
        // 权限集合
        Set<String> permissions = loginService.getMenuPermission(loginUser.getEnterpriseId(), loginUser.getSysUser().getUserId(), loginUser.getSysUser().getUserType());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userService.selectUserById(loginUser.getSysUser().getUserId()));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取用户列表
     */
    @PreAuthorize(hasPermi = "system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据用户Id获取详细信息
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = {"/","/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        return AjaxResult.success(userService.selectUserById(userId));
    }

    /**
     * 新增用户
     */
    @PreAuthorize(hasPermi = "system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserCodeUnique(user.getUserId(), user.getUserCode()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserId(), user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getUserId(), user.getPhone()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getUserId(), user.getEmail()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user.getUserType());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserCodeUnique(user.getUserId(), user.getUserCode()))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，用户编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserId(), user.getUserName()))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，用户账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getUserId(), user.getPhone()))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getUserId(), user.getEmail()))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    /**
     * 修改部门-角色关系
     */
    @PreAuthorize(hasPermi = "system:role:set")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeUserRole")
    public AjaxResult editUserRole(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user.getUserType());
        return toAjax(userService.updateUserRole(user.getUserId(), user.getRoleIds()));
    }

    /**
     * 重置密码
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user.getUserType());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.resetUserPwd(user.getUserId(), user.getPassword()));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user.getUserType());
        if (StringUtils.equals(UserConstants.USER_NORMAL, user.getStatus())
                && UserConstants.POST_DISABLE.equals(postService.checkPostStatus(user.getPostId()))) {
            return AjaxResult.error("启用失败，该用户的归属岗位已被禁用！");
        }
        return toAjax(userService.updateUserStatus(user.getUserId(), user.getStatus()));
    }

    /**
     * 删除用户
     */
    @PreAuthorize(hasPermi = "system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 导出用户
     */
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) throws IOException {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 导入用户
     */
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize(hasPermi = "system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 导入模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }
}
