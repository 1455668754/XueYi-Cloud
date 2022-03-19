package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantModuleMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysAuthManager {

    @Autowired
    private SysModuleManager moduleManager;

    @Autowired
    private SysMenuManager menuManager;

    @Autowired
    private SysTenantModuleMergeMapper tenantModuleMergeMapper;

    @Autowired
    private SysTenantMenuMergeMapper tenantMenuMergeMapper;

    @Autowired
    private SysRoleModuleMergeMapper roleModuleMergeMapper;

    @Autowired
    private SysRoleMenuMergeMapper roleMenuMergeMapper;

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectCommonAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectCommonList();
        List<SysMenuDto> menus = menuManager.selectCommonList();
        return new ArrayList<>(CollUtil.addAll(
                modules.stream().map(SysAuthTree::new).collect(Collectors.toList()),
                menus.stream().map(SysAuthTree::new).collect(Collectors.toList())));
    }

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectEnterpriseAuthScope() {
        List<SysModuleDto> modules = moduleManager.selectTenantList();
        List<SysMenuDto> menus = menuManager.selectTenantList();
        return new ArrayList<>(CollUtil.addAll(
                modules.stream().map(SysAuthTree::new).collect(Collectors.toList()),
                menus.stream().map(SysAuthTree::new).collect(Collectors.toList())));
    }

    /**
     * 获取租户权限对象集合
     *
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectTenantAuth() {
        List<SysAuthTree> list = new ArrayList<>();
        // 1.获取拥有权限的模块
        List<SysTenantModuleMerge> tenantModuleMerges = tenantModuleMergeMapper.selectList(Wrappers.query());
        if (CollUtil.isNotEmpty(tenantModuleMerges)) {
            List<Long> moduleIds = tenantModuleMerges.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(moduleIds);
            list.addAll(moduleList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        // 2.获取拥有权限的菜单
        List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
        if (CollUtil.isNotEmpty(tenantMenuMerges)) {
            List<Long> menuIds = tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
            List<SysMenuDto> menuList = menuManager.selectListByIds(menuIds);
            list.addAll(menuList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 获取角色权限对象集合
     *
     * @param roleId 角色Id
     * @return 权限对象集合
     */
    public List<SysAuthTree> selectRoleAuth(Long roleId) {
        List<SysAuthTree> list = new ArrayList<>();
        // 1.获取拥有权限的模块
        List<SysRoleModuleMerge> roleModuleMerges = roleModuleMergeMapper.selectList(
                Wrappers.<SysRoleModuleMerge>query().lambda()
                        .eq(SysRoleModuleMerge::getRoleId, roleId));
        if (CollUtil.isNotEmpty(roleModuleMerges)) {
            List<Long> moduleIds = roleModuleMerges.stream().map(SysRoleModuleMerge::getModuleId).collect(Collectors.toList());
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(moduleIds);
            list.addAll(moduleList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        // 2.获取拥有权限的菜单
        List<SysRoleMenuMerge> roleMenuMerges = roleMenuMergeMapper.selectList(
                Wrappers.<SysRoleMenuMerge>query().lambda()
                        .eq(SysRoleMenuMerge::getRoleId, roleId));
        if (CollUtil.isNotEmpty(roleMenuMerges)) {
            List<Long> menuIds = roleMenuMerges.stream().map(SysRoleMenuMerge::getMenuId).collect(Collectors.toList());
            List<SysMenuDto> menuList = menuManager.selectListByIds(menuIds);
            list.addAll(menuList.stream().map(SysAuthTree::new).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 新增租户权限
     *
     * @param authIds 权限Ids
     */
    public void addTenantAuth(Long[] authIds) {
        List<Long> menuIdList = new ArrayList<>(Arrays.asList(authIds));
        if (CollUtil.isNotEmpty(menuIdList)) {
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(menuIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                // 1.权限Ids中的模块Ids与菜单Ids分开
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                menuIdList.removeAll(moduleIdList);
                // 2.存储租户与模块的关联数据
                List<SysTenantModuleMerge> tenantModuleMerges = moduleIdList.stream().map(SysTenantModuleMerge::new).collect(Collectors.toList());
                tenantModuleMergeMapper.insertBatch(tenantModuleMerges);
            }
            // 3.存储租户与菜单的关联数据
            List<SysTenantMenuMerge> tenantMenuMerges = menuIdList.stream().map(SysTenantMenuMerge::new).collect(Collectors.toList());
            tenantMenuMergeMapper.insertBatch(tenantMenuMerges);
        }
    }

    /**
     * 修改租户权限
     *
     * @param authIds 权限Ids
     */
    public void editTenantAuth(Long[] authIds) {
        List<Long> menuIdList = new ArrayList<>(Arrays.asList(authIds));
        // 1.校验authIds是否为空? 删除不存在的,增加新增的 : 删除所有
        if (CollUtil.isNotEmpty(menuIdList)) {
            // 2.查询authIds中的模块Id，分离menuIds与moduleIds
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(menuIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                menuIdList.removeAll(moduleIdList);
                // 3.查询原始的租户与模块关联数据,新增/删除差异关联数据
                List<SysTenantModuleMerge> originalModuleList = tenantModuleMergeMapper.selectList(Wrappers.query());
                if (CollUtil.isNotEmpty(originalModuleList)) {
                    List<Long> originalModuleIds = originalModuleList.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
                    List<Long> delModuleIds = new ArrayList<>(originalModuleIds);
                    delModuleIds.removeAll(moduleIdList);
                    if (CollUtil.isNotEmpty(delModuleIds)) {
                        tenantModuleMergeMapper.delete(
                                Wrappers.<SysTenantModuleMerge>query().lambda()
                                        .in(SysTenantModuleMerge::getModuleId, delModuleIds));
                        roleModuleMergeMapper.delete(
                                Wrappers.<SysRoleModuleMerge>query().lambda()
                                        .in(SysRoleModuleMerge::getModuleId, delModuleIds));
                    }
                    moduleIdList.removeAll(originalModuleIds);
                }
                if (CollUtil.isNotEmpty(moduleIdList)) {
                    List<SysTenantModuleMerge> tenantModuleMerges = moduleIdList.stream().map(SysTenantModuleMerge::new).collect(Collectors.toList());
                    tenantModuleMergeMapper.insertBatch(tenantModuleMerges);
                }
            }
            // // 4.查询原始的租户与菜单关联数据,新增/删除差异关联数据
            List<SysTenantMenuMerge> originalMenuList = tenantMenuMergeMapper.selectList(Wrappers.query());
            if (CollUtil.isNotEmpty(originalMenuList)) {
                List<Long> originalMenuIds = originalMenuList.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
                List<Long> delMenuIds = new ArrayList<>(originalMenuIds);
                delMenuIds.removeAll(menuIdList);
                if (CollUtil.isNotEmpty(delMenuIds)) {
                    tenantMenuMergeMapper.delete(
                            Wrappers.<SysTenantMenuMerge>query().lambda()
                                    .in(SysTenantMenuMerge::getMenuId, delMenuIds));
                    roleMenuMergeMapper.delete(
                            Wrappers.<SysRoleMenuMerge>query().lambda()
                                    .in(SysRoleMenuMerge::getMenuId, delMenuIds));
                }
                menuIdList.removeAll(originalMenuIds);
            }
            if (CollUtil.isNotEmpty(menuIdList)) {
                List<SysTenantMenuMerge> tenantMenuMerges = menuIdList.stream().map(SysTenantMenuMerge::new).collect(Collectors.toList());
                tenantMenuMergeMapper.insertBatch(tenantMenuMerges);
            }
        } else {
            List<SysTenantModuleMerge> originalModuleList = tenantModuleMergeMapper.selectList(Wrappers.query());
            if (CollUtil.isNotEmpty(originalModuleList)) {
                List<Long> originalModuleIds = originalModuleList.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(originalModuleIds)) {
                    tenantModuleMergeMapper.delete(
                            Wrappers.<SysTenantModuleMerge>query().lambda()
                                    .in(SysTenantModuleMerge::getModuleId, originalModuleIds));
                    roleModuleMergeMapper.delete(
                            Wrappers.<SysRoleModuleMerge>query().lambda()
                                    .in(SysRoleModuleMerge::getModuleId, originalModuleIds));
                }
            }
            List<SysTenantMenuMerge> originalMenuList = tenantMenuMergeMapper.selectList(Wrappers.query());
            List<Long> originalMenuIds = originalMenuList.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(originalMenuIds)) {
                tenantMenuMergeMapper.delete(
                        Wrappers.<SysTenantMenuMerge>query().lambda()
                                .in(SysTenantMenuMerge::getMenuId, originalMenuIds));
                roleMenuMergeMapper.delete(
                        Wrappers.<SysRoleMenuMerge>query().lambda()
                                .in(SysRoleMenuMerge::getMenuId, originalMenuIds));
            }
        }
    }

    /**
     * 新增角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    public void addRoleAuth(Long roleId, Long[] authIds) {
        List<Long> authIdList = new ArrayList<>(Arrays.asList(authIds));
        if (CollUtil.isNotEmpty(authIdList)) {
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(authIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                // 1.权限Ids中的模块Ids与菜单Ids分开
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                authIdList.removeAll(moduleIdList);
                // 2.存储角色与模块的关联数据
                List<SysRoleModuleMerge> roleModuleMerges = moduleIdList.stream().map(moduleId -> new SysRoleModuleMerge(roleId, moduleId)).collect(Collectors.toList());
                roleModuleMergeMapper.insertBatch(roleModuleMerges);
            }
            // 3.存储角色与菜单的关联数据
            List<SysRoleMenuMerge> roleMenuMerges = authIdList.stream().map(menuId -> new SysRoleMenuMerge(roleId, menuId)).collect(Collectors.toList());
            roleMenuMergeMapper.insertBatch(roleMenuMerges);
        }
    }

    /**
     * 修改角色权限
     *
     * @param roleId  角色Id
     * @param authIds 权限Ids
     */
    public void editRoleAuth(Long roleId, Long[] authIds) {
        List<Long> authIdList = new ArrayList<>(Arrays.asList(authIds));
        // 1.校验authIds是否为空? 删除不存在的,增加新增的 : 删除所有
        if (CollUtil.isNotEmpty(authIdList)) {
            // 2.查询authIds中的模块Id，分离menuIds与moduleIds
            List<SysModuleDto> moduleList = moduleManager.selectListByIds(authIdList);
            if (CollUtil.isNotEmpty(moduleList)) {
                List<Long> moduleIdList = moduleList.stream().map(SysModuleDto::getId).collect(Collectors.toList());
                authIdList.removeAll(moduleIdList);
                // 3.查询原始的角色与模块关联数据,新增/删除差异关联数据
                List<SysRoleModuleMerge> originalModuleList = roleModuleMergeMapper.selectList(
                        Wrappers.<SysRoleModuleMerge>query().lambda()
                                .eq(SysRoleModuleMerge::getRoleId, roleId));
                if (CollUtil.isNotEmpty(originalModuleList)) {
                    List<Long> originalModuleIds = originalModuleList.stream().map(SysRoleModuleMerge::getModuleId).collect(Collectors.toList());
                    List<Long> delModuleIds = new ArrayList<>(originalModuleIds);
                    delModuleIds.removeAll(moduleIdList);
                    if (CollUtil.isNotEmpty(delModuleIds)) {
                        roleModuleMergeMapper.delete(
                                Wrappers.<SysRoleModuleMerge>query().lambda()
                                        .eq(SysRoleModuleMerge::getRoleId, roleId)
                                        .in(SysRoleModuleMerge::getModuleId, delModuleIds));
                    }
                    moduleIdList.removeAll(originalModuleIds);
                }
                if (CollUtil.isNotEmpty(moduleIdList)) {
                    List<SysRoleModuleMerge> roleModuleMerges = moduleIdList.stream().map(moduleId -> new SysRoleModuleMerge(roleId, moduleId)).collect(Collectors.toList());
                    roleModuleMergeMapper.insertBatch(roleModuleMerges);
                }
            }
            // // 4.查询原始的角色与菜单关联数据,新增/删除差异关联数据
            List<SysRoleMenuMerge> originalMenuList = roleMenuMergeMapper.selectList(
                    Wrappers.<SysRoleMenuMerge>query().lambda()
                            .eq(SysRoleMenuMerge::getRoleId, roleId));
            if (CollUtil.isNotEmpty(originalMenuList)) {
                List<Long> originalMenuIds = originalMenuList.stream().map(SysRoleMenuMerge::getMenuId).collect(Collectors.toList());
                List<Long> delMenuIds = new ArrayList<>(originalMenuIds);
                delMenuIds.removeAll(authIdList);
                if (CollUtil.isNotEmpty(delMenuIds)) {
                    roleMenuMergeMapper.delete(
                            Wrappers.<SysRoleMenuMerge>query().lambda()
                                    .eq(SysRoleMenuMerge::getRoleId, roleId)
                                    .in(SysRoleMenuMerge::getMenuId, delMenuIds));
                }
                authIdList.removeAll(originalMenuIds);
            }
            if (CollUtil.isNotEmpty(authIdList)) {
                List<SysRoleMenuMerge> roleMenuMerges = authIdList.stream().map(menuId -> new SysRoleMenuMerge(roleId, menuId)).collect(Collectors.toList());
                roleMenuMergeMapper.insertBatch(roleMenuMerges);
            }
        } else {
            roleModuleMergeMapper.delete(
                    Wrappers.<SysRoleModuleMerge>query().lambda()
                            .eq(SysRoleModuleMerge::getRoleId, roleId));
            roleMenuMergeMapper.delete(
                    Wrappers.<SysRoleMenuMerge>query().lambda()
                            .in(SysRoleMenuMerge::getRoleId, roleId));
        }
    }
}
