package com.xueyi.system.authority.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.authority.manager.SysMenuManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 菜单管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysMenuServiceImpl extends TreeServiceImpl<SysMenuDto, SysMenuManager, SysMenuMapper> implements ISysMenuService {

    /**
     * 登录校验 | 获取当前租户全部菜单权限标识集合
     *
     * @return 菜单权限集合
     */
    @Override
    public Set<String> loginPermission() {
        List<SysMenuDto> menuList = baseManager.loginMenuList();
        return CollUtil.isNotEmpty(menuList)
                ? menuList.stream().map(SysMenuDto::getPerms).collect(Collectors.toSet())
                : new HashSet<>();
    }

    /**
     * 登录校验 | 获取菜单权限标识集合
     *
     * @param roleIds 角色Id集合
     * @return 菜单权限集合
     */
    @Override
    public Set<String> loginPermission(Set<Long> roleIds) {
        List<SysMenuDto> menuList = baseManager.loginMenuList(roleIds);
        return CollUtil.isNotEmpty(menuList)
                ? menuList.stream().map(SysMenuDto::getPerms).collect(Collectors.toSet())
                : new HashSet<>();
    }

    /**
     * 登录校验 | 获取全部路由路径集合
     *
     * @return 路径集合
     */
    @Override
    public Map<String, String> getLessorRouteMap() {
        List<SysMenuDto> menuList = baseManager.loginLessorMenuList()
                .stream().filter(menu -> ObjectUtil.notEqual(menu.getId(), AuthorityConstants.MENU_TOP_NODE) && (menu.isDir() || menu.isMenu() || menu.isDetails()))
                .collect(Collectors.toList());
        return buildRoutePath(menuList);
    }

    /**
     * 登录校验 | 获取租户全部路由路径集合
     *
     * @return 路径集合
     */
    @Override
    public Map<String, String> getRouteMap() {
        List<SysMenuDto> menuList = baseManager.loginMenuList()
                .stream().filter(menu -> ObjectUtil.notEqual(menu.getId(), AuthorityConstants.MENU_TOP_NODE) && (menu.isDir() || menu.isMenu() || menu.isDetails()))
                .collect(Collectors.toList());
        return buildRoutePath(menuList);
    }

    /**
     * 登录校验 | 获取路由路径集合
     *
     * @param roleIds 角色Id集合
     * @return 路径集合
     */
    @Override
    public Map<String, String> getRouteMap(Set<Long> roleIds) {
        List<SysMenuDto> menuList = baseManager.loginMenuList(roleIds)
                .stream().filter(menu -> ObjectUtil.notEqual(menu.getId(), AuthorityConstants.MENU_TOP_NODE) && (menu.isDir() || menu.isMenu() || menu.isDetails()))
                .collect(Collectors.toList());
        return buildRoutePath(menuList);
    }

    /**
     * 查询菜单对象列表 | 数据权限 | 附加数据
     *
     * @param menu 菜单对象
     * @return 菜单对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysMenuMapper"})
    public List<SysMenuDto> selectListScope(SysMenuDto menu) {
        return baseManager.selectListExtra(menu);
    }

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId) {
        return baseManager.getRoutes(moduleId);
    }

    /**
     * 根据菜单类型及模块Id获取可配菜单集
     *
     * @param moduleId 模块Id
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getMenuByMenuType(Long moduleId, String menuType) {
        return baseManager.getMenuByMenuType(moduleId, menuType);
    }

    /**
     * 新增菜单对象
     *
     * @param menu 菜单对象
     * @return 结果
     */
    @Override
    public int insert(SysMenuDto menu) {
        menu.setName(IdUtil.simpleUUID());
        return super.insert(menu);
    }

    /**
     * 新增菜单对象（批量）
     *
     * @param menuList 菜单对象集合
     * @return 结果
     */
    @Override
    public int insertBatch(Collection<SysMenuDto> menuList) {
        if (CollUtil.isNotEmpty(menuList))
            menuList.forEach(menu -> menu.setName(IdUtil.simpleUUID()));
        return super.insertBatch(menuList);
    }

    /**
     * 校验菜单是否存在租户
     *
     * @param id 菜单Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkMenuExistTenant(Long id) {
        return ObjectUtil.isNotNull(baseManager.checkMenuExistTenant(id));
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param id 菜单Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkMenuExistRole(Long id) {
        return ObjectUtil.isNotNull(baseManager.checkMenuExistRole(id));
    }

    /**
     * 构建路由路径集合
     *
     * @param menus 菜单列表
     * @return 路径集合
     */
    private Map<String, String> buildRoutePath(List<SysMenuDto> menus) {
        Map<String, String> routeMap = new HashMap<>();
        if (CollUtil.isEmpty(menus))
            return new HashMap<>();
        List<SysMenuDto> menuTree = TreeUtils.buildTree(menus);
        SysMenuDto menu = new SysMenuDto();
        menu.setFullPath(StrUtil.EMPTY);
        menu.setChildren(menuTree);
        menuTree.forEach(sonChild -> {
            if (sonChild.isDetails())
                routeMap.put(sonChild.getName(), StrUtil.SLASH + sonChild.getPath());
        });
        recursionFn(menu, routeMap);
        return routeMap;
    }

    /**
     * 递归菜单树
     *
     * @param menu     菜单对象
     * @param routeMap 路径集合
     */
    private void recursionFn(SysMenuDto menu, Map<String, String> routeMap) {
        if (CollUtil.isNotEmpty(menu.getChildren())) {
            menu.getChildren().forEach(sonChild -> {
                sonChild.setFullPath(menu.getFullPath() + StrUtil.SLASH + sonChild.getPath());
                if (!sonChild.isDetails())
                    routeMap.put(sonChild.getName(), sonChild.getFullPath());
                if (CollUtil.isNotEmpty(sonChild.getChildren())) {
                    sonChild.getChildren().forEach(grandsonChild -> {
                        if (grandsonChild.isDetails()) {
                            String detailsSuffix = grandsonChild.getDetailsSuffix();
                            grandsonChild.setFullPath(StrUtil.isNotEmpty(detailsSuffix) ? menu.getFullPath() + StrUtil.SLASH + detailsSuffix : menu.getFullPath());
                            routeMap.put(grandsonChild.getName(), grandsonChild.getFullPath());
                        }
                    });
                    recursionFn(sonChild, routeMap);
                }
            });
        }
    }
    
    /**
     * 检验祖籍或归属模块是否变更
     * 是否变更，变更则同步变更子菜单祖籍及其归属模块
     *
     * @param menu 菜单对象 | id id | parentId 父Id | moduleId 模块Id
     */
    @Override
    protected void UHandleAncestorsCheck(SysMenuDto menu) {
        SysMenuDto original = baseManager.selectById(menu.getId());
        if (ObjectUtil.notEqual(menu.getParentId(), original.getParentId()) || ObjectUtil.notEqual(menu.getModuleId(), original.getModuleId())) {
            String oldAncestors = original.getAncestors();
            if (ObjectUtil.equals(BaseConstants.TOP_ID, menu.getParentId())) {
                menu.setAncestors(String.valueOf(BaseConstants.TOP_ID));
            } else {
                SysMenuDto parent = baseManager.selectById(menu.getParentId());
                menu.setAncestors(parent.getAncestors() + StrUtil.COMMA + menu.getParentId());
            }
            baseManager.updateChildrenAncestors(menu.getId(), menu.getAncestors(), oldAncestors, menu.getModuleId());
        }
    }
}
