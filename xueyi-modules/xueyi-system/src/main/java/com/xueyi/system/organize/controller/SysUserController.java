package com.xueyi.system.organize.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.organize.service.ISysUserService;
import com.xueyi.system.api.domain.source.Source;
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
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.domain.organize.SysUser;
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

    @Autowired
    private ISysEnterpriseService enterpriseService;

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{enterpriseName}/{userName}")
    public R<LoginUser> info(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName) {
        SysEnterprise checkEnterprise = new SysEnterprise();
        checkEnterprise.setEnterpriseName(enterpriseName);
        SysEnterprise sysEnterprise = loginService.checkLoginByEnterpriseName(checkEnterprise);
        if (StringUtils.isNull(sysEnterprise)) {
            return R.fail("账号或密码错误，请检查");
        }
        //查询租户所有的主从库信息
        Source checkSource = new Source();
        checkSource.setEnterpriseId(sysEnterprise.getEnterpriseId());
        List<Source> source = enterpriseService.selectLoadDataSources(checkSource);
        Source master = new Source();
        for (Source s : source) {
            if (s.getIsMain().equals("Y")) {
                master = s;
                break;
            }
        }
        //开始进入对应的主数据库
        SysUser checkUser = new SysUser();
        checkUser.setSourceName(master.getMaster());
        checkUser.setEnterpriseId(sysEnterprise.getEnterpriseId());
        checkUser.setUserName(userName);
        SysUser sysUser = loginService.checkLoginByEnterpriseIdANDUserName(checkUser);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("账号或密码错误，请检查");
        }
        // 角色集合
        SysRole checkRole = new SysRole();
        checkRole.setEnterpriseId(sysEnterprise.getEnterpriseId());
        checkRole.getParams().put("deptId", sysUser.getDeptId());
        checkRole.getParams().put("postId", sysUser.getPostId());
        checkRole.getParams().put("userId", sysUser.getUserId());
        Set<String> roles = loginService.getRolePermission(master.getMaster(), checkRole, sysUser.getUserType());
        // 权限集合
        SysMenu checkMenu = new SysMenu();
        checkMenu.setEnterpriseId(sysEnterprise.getEnterpriseId());
        checkMenu.getParams().put("userId", sysUser.getUserId());
        Set<String> permissions = loginService.getMenuPermission(master.getMaster(), checkMenu, sysUser.getUserType());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setMainSource(master.getMaster());
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setUserType(sysUser.getUserType());
        sysUserVo.setSysEnterprise(sysEnterprise);
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
        SysRole checkRole = new SysRole();
        checkRole.setEnterpriseId(loginUser.getEnterpriseId());
        checkRole.getParams().put("deptId", loginUser.getSysUser().getDeptId());
        checkRole.getParams().put("postId", loginUser.getSysUser().getPostId());
        checkRole.getParams().put("userId", loginUser.getSysUser().getUserId());
        Set<String> roles = loginService.getRolePermission(loginUser.getMainSource(), checkRole, loginUser.getSysUser().getUserType());
        // 权限集合
        SysMenu checkMenu = new SysMenu();
        checkMenu.setEnterpriseId(loginUser.getEnterpriseId());
        checkMenu.getParams().put("userId", loginUser.getSysUser().getUserId());
        Set<String> permissions = loginService.getMenuPermission(loginUser.getMainSource(), checkMenu, loginUser.getSysUser().getUserType());
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
        SysUser user = new SysUser();
        user.setUserId(userId);
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
        SysPost post = new SysPost();
        post.setPostId(user.getPostId());
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
        List<Long> Ids = (List<Long>) user.getParams().get("Ids");
        for (int i = Ids.size() - 1; i >= 0; i--) {
            if(Objects.equals(SecurityUtils.getUserId(), Long.valueOf(String.valueOf(Ids.get(i))))){
                Ids.remove(i);
                if(Ids.size()<=0){
                    return AjaxResult.error("删除失败，不能删除自己！");
                }else{
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