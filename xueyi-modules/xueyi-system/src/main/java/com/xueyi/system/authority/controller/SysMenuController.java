package com.xueyi.system.authority.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends TreeController<SysMenuDto, ISysMenuService> {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysModuleService moduleService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "菜单";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "模块";
    }

    /**
     * 获取当前节点及其祖籍信息 | 内部调用
     */
    @InnerAuth
    @GetMapping("/inner/{id}")
    public R<SysMenuDto> getInfoInner(@PathVariable Serializable id) {
        return R.ok(baseService.selectById(id));
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getRouters/{moduleId}")
    public AjaxResult getRouters(@PathVariable Long moduleId) {
        LoginUser loginUser = tokenService.getLoginUser();
        Map<String, Object> menuMap = loginUser.getMenuRoute();
        if (ObjectUtil.isNull(menuMap.get(moduleId.toString()))) {
            List<SysMenuDto> menus = baseService.getRoutes(moduleId);
            menuMap.put(moduleId.toString(), baseService.buildMenus(TreeUtils.buildTree(menus)));
            tokenService.setLoginUser(loginUser);
        }
        return AjaxResult.success(menuMap.get(moduleId.toString()));
    }

    /**
     * 查询菜单列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_MENU_LIST)
    public AjaxResult list(SysMenuDto menu) {
        return super.list(menu);
    }

    /**
     * 查询菜单列表（排除节点）
     */
    @GetMapping("/list/exclude")
    @RequiresPermissions(Auth.SYS_MENU_LIST)
    public AjaxResult listExNodes(SysMenuDto menu) {
        return super.listExNodes(menu);
    }

    /**
     * 查询菜单详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_MENU_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集
     */
    @PostMapping("/routeList")
    public AjaxResult getMenuByMenuType(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集（排除节点）
     */
    @PostMapping("/routeList/exclude")
    public AjaxResult getMenuByMenuTypeExNodes(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        Iterator<SysMenuDto> it = menus.iterator();
        while (it.hasNext()) {
            SysMenuDto next = (SysMenuDto) it.next();
            if (ObjectUtil.equals(next.getId(), menu.getId()) ||
                    ArrayUtils.contains(StringUtils.split(next.getAncestors(), StrUtil.COMMA), menu.getId() + StrUtil.EMPTY))
                it.remove();
        }
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 菜单新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_MENU_ADD)
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysMenuDto menu) {
        return super.add(menu);
    }

    /**
     * 菜单修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_MENU_EDIT)
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysMenuDto menu) {
        return super.edit(menu);
    }

    /**
     * 菜单修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_MENU_EDIT, Auth.SYS_MENU_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysMenuDto menu) {
        return super.editStatus(menu);
    }

    /**
     * 菜单批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_MENU_DELETE)
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取菜单选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandleValidated(BaseConstants.Operate operate, SysMenuDto menu) {
        if (ObjectUtil.equals(menu.getId(), AuthorityConstants.MENU_TOP_NODE))
            throw new ServiceException(StrUtil.format("默认{}不允许修改！", getNodeName()));
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName()));
        if (operate.isAdd() && SecurityUtils.isNotAdminTenant() && menu.isCommon())
            throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
        if (operate.isEdit()) {
            SysMenuDto original = baseService.selectById(menu.getId());
            if (ObjectUtil.isNull(original))
                throw new ServiceException("数据不存在！");
            if (SecurityUtils.isNotAdminTenant() && original.isCommon())
                throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
            if (!StringUtils.equals(menu.getIsCommon(), original.getIsCommon()))
                throw new ServiceException(StrUtil.format("{}{}{}失败，公共{}属性禁止变更！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName()));
        }
        if (menu.isCommon()) {
            SysModuleDto module = moduleService.selectById(menu.getModuleId());
            if (ObjectUtil.isNull(module))
                throw new ServiceException("数据不存在！");
            if (module.isNotCommon())
                throw new ServiceException(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getParentName()));
            SysMenuDto parentMenu = baseService.selectById(menu.getParentId());
            if (ObjectUtil.isNull(parentMenu))
                throw new ServiceException("数据不存在！");
            if (parentMenu.isNotCommon())
                throw new ServiceException(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getNodeName()));
        }

        // 待移除：混合逻辑完成后移除
        if (operate.isAdd() && menu.isCommon()) {
            menu.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID);
        }
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    @Override
    protected void RHandleValidated(BaseConstants.Operate operate, List<Long> idList) {
        // remove top node
        for (int i = idList.size() - 1; i >= 0; i--)
            if (ObjectUtil.equals(idList.get(i), AuthorityConstants.MENU_TOP_NODE))
                idList.remove(i);
        if (CollUtil.isEmpty(idList))
            throw new ServiceException(StrUtil.format("删除失败，无法删除默认{}！", getNodeName()));
    }
}
