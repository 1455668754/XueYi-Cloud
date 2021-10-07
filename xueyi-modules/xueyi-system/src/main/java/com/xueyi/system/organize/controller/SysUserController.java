package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.ParamsUtils;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysEnterpriseService enterpriseService;

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{enterpriseName}/{userName}")
    public R<LoginUser> info(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName) {
        SysEnterprise enterprise = enterpriseService.mainGetEnterpriseProfileByEnterpriseName(enterpriseName);
        if (StringUtils.isNull(enterprise)) {
            return R.fail("账号或密码错误，请检查");
        }
        //查询租户的主从库信息
        Source source = DataSourceUtils.getSourceByEnterpriseId(enterprise.getEnterpriseId());
        //开始进入对应的主数据库
        SysUser checkUser = new SysUser();
        checkUser.setSourceName(source.getMaster());
        checkUser.setEnterpriseId(enterprise.getEnterpriseId());
        checkUser.setUserName(userName);
        SysUser sysUser = loginService.checkLoginByEnterpriseIdANDUserName(checkUser);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("账号或密码错误，请检查");
        }
        // 角色集合
        List<SysRole> roleList = roleService.getRoleListByUserId(sysUser.getUserId(), enterprise.getEnterpriseId(), source.getMaster());
        Set<Long> roles = roleList.stream().map(SysRole::getRoleId).collect(Collectors.toSet());
        Set<String> roleKeys = loginService.getRolePermission(roles, sysUser.getUserType(), enterprise.getEnterpriseId());
        // 权限集合
        Set<String> permissions = loginService.getMenuPermission(roles, sysUser.getUserType(), enterprise.getEnterpriseId());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setMainSource(source.getMaster());
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setUserType(sysUser.getUserType());
        sysUserVo.setSysEnterprise(enterprise);
        sysUserVo.setRoleKeys(roleKeys);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        sysUserVo.setSource(source);
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
        Set<String> roles = loginService.getRolePermission(loginUser.getRoles(), loginUser.getSysUser().getUserType(), loginUser.getEnterpriseId());
        // 权限集合
        Set<String> permissions = loginService.getMenuPermission(loginUser.getRoles(), loginUser.getSysUser().getUserType(), loginUser.getEnterpriseId());
        AjaxResult ajax = AjaxResult.success();
        SysUser user = new SysUser();
        user.setUserId(loginUser.getSysUser().getUserId());
        ajax.put("user", userService.selectUserById(user));
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
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        SysUser user = new SysUser(userId);
        return AjaxResult.success(userService.selectUserById(user));
    }

    /**
     * 新增用户
     */
    @PreAuthorize(hasPermi = "system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserCodeUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
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
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserCodeUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，用户编码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，用户账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
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
        userService.checkUserAllowed(user);
        return toAjax(userService.updateUserRole(user));
    }

    /**
     * 重置密码
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.resetUserPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        SysPost post = new SysPost(user.getPostId());
        if (StringUtils.equals(UserConstants.USER_NORMAL, user.getStatus())
                && UserConstants.POST_DISABLE.equals(postService.checkPostStatus(post))) {
            return AjaxResult.error("启用失败，该用户的归属岗位已被禁用！");
        }
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize(hasPermi = "system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult remove(@RequestBody SysUser user) {
        List<Long> Ids = ParamsUtils.IdsObjectToLongList(user.getParams().get("Ids"));
        for (int i = Ids.size() - 1; i >= 0; i--) {
            if (Objects.equals(SecurityUtils.getUserId(), Ids.get(i))) {
                Ids.remove(i);
                if (Ids.size() <= 0) {
                    return AjaxResult.error("删除失败，不能删除自己！");
                } else {
                    userService.deleteUserByIds(user);
                    return AjaxResult.error("删除成功但未删除自己信息！");
                }
            }
        }
        return toAjax(userService.deleteUserByIds(user));
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
        String operName = SecurityUtils.getUserName();
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