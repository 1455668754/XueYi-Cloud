package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.SortUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.redis.utils.AuthorityUtils;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.domain.authority.SysMenu;
import com.xueyi.system.authority.domain.SystemMenuVo;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysSystemMapper;
import com.xueyi.system.authority.service.ISysAuthorityService;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.api.domain.role.SysRoleSystemMenu;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import com.xueyi.system.role.service.ISysRoleSystemMenuService;
import com.xueyi.system.utils.vo.TreeSelect;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 子系统模块Service业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SysSystemServiceImpl implements ISysSystemService {

    @Autowired
    private ISysAuthorityService authorityService;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysSystemMapper systemMapper;

    @Autowired
    private ISysRoleSystemMenuService roleSystemMenuService;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    /**
     * 当前用户首页可展示的模块列表
     *
     * @return 子系统模块集合
     */
    @Override
    public List<SysSystem> getSystemRoutes() {
        Set<SysSystem> systemSet = AuthorityUtils.getSystemCache(SecurityUtils.getEnterpriseId());
        Set<SysSystem> rangeSet;
        // 管理员显示所有模块信息
        if (SecurityUtils.isAdminUser()) {
            rangeSet = authorityService.selectSystemSet(authorityService.selectRoleListByTenantId(SecurityUtils.getEnterpriseId()), SecurityUtils.isAdminTenant(), false);
        } else {
            SysRole role = new SysRole();
            role.getParams().put("userId", SecurityUtils.getUserId());
            rangeSet = authorityService.selectSystemSet(authorityService.selectRoleListByUserId(role), SecurityUtils.isAdminTenant(), true);
        }
        systemSet.retainAll(rangeSet);
        return SortUtils.sortSetToList(systemSet);
    }

    /**
     * 根据用户Id查询模块&&菜单 | 非管理员
     *
     * @return 模块&&菜单列表
     */
    @Override
    @DS("#isolate")
    @DataScope(eAlias = "rsm")
    public List<SysRoleSystemMenu> userHomePageView(SysMenu menu) {
        return roleSystemMenuMapper.selectSystemMenuListByUserId(menu);
    }

    /**
     * 查询子系统模块
     *
     * @param system 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    @Override
    public SysSystem selectSystemById(SysSystem system) {
        return systemMapper.selectSystemById(system);
    }

    /**
     * 查询子系统模块列表
     *
     * @param system 子系统模块
     * @return 子系统模块
     */
    @Override
    public List<SysSystem> selectSystemList(SysSystem system) {
        return systemMapper.selectSystemList(system);
    }

    /**
     * 新增子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    @Override
    public int insertSystem(SysSystem system) {
        return systemMapper.insertSystem(system);
    }

    /**
     * 修改子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    @Override
    public int updateSystem(SysSystem system) {
        return systemMapper.updateSystem(system);
    }

    /**
     * 修改子系统模块状态
     *
     * @param system 子系统模块
     * @return 结果
     */
    @Override
    public int updateSystemStatus(SysSystem system) {
        return systemMapper.updateSystemStatus(system);
    }

    /**
     * 批量删除子系统模块
     *
     * @param system 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    @Override
    public int deleteSystemByIds(SysSystem system) {
        return systemMapper.deleteSystemByIds(system);
    }

    /**
     * 加载角色系统-菜单列表树
     *
     * @param system 子系统模块 | Id exclude的模块&菜单Id | status 模块&菜单状态 | searchValue 查询类型
     *               searchValue = PERMIT_ALL                        获取所有权限内模块&菜单 | 无衍生角色
     *               searchValue = PERMIT_ALL_ONLY_PUBLIC            获取所有权限内模块&菜单 | 无衍生角色 | 仅公共数据
     *               searchValue = PERMIT_ADMINISTRATOR              仅获取超管权限内模块&菜单 | 衍生角色仅获取超管衍生
     *               searchValue = PERMIT_ENTERPRISE                 仅获取租户权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *               searchValue = PERMIT_PERSONAL_SCREEN_DERIVE     仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *               searchValue = PERMIT_PERSONAL                   仅获取个人权限内模块&菜单 | 衍生角色获取自身组织衍生&超管衍生&租户衍生
     */
    @Override
    @Transactional
    @GlobalTransactional
    public List<TreeSelect> buildSystemMenuTreeSelect(SysSystem system) {
        List<SysRoleSystemMenu> list = null;
        SysSystem checkSystem = new SysSystem();
        SysMenu checkMenu = new SysMenu();
        if (StringUtils.isNotEmpty(system.getSearchValue()) && !StringUtils.equalsAny(system.getSearchValue(), MenuConstants.PERMIT_ALL, MenuConstants.PERMIT_ALL_ONLY_PUBLIC)) {
            if (SysUser.isAdmin(SecurityUtils.getUserType()) || StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_ADMINISTRATOR)) {
                list = roleSystemMenuService.selectPermitAdministrator(new SysRoleSystemMenu());
            } else if (StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_ENTERPRISE)) {
                list = roleSystemMenuService.selectPermitEnterprise(new SysRoleSystemMenu());
            } else if (StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_PERSONAL_SCREEN_DERIVE)) {
                SysRoleSystemMenu systemMenu = new SysRoleSystemMenu();
                systemMenu.getParams().put("userId", SecurityUtils.getUserId());
                list = roleSystemMenuService.selectPermitPersonalScreenDerive(systemMenu);
            } else if (StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_PERSONAL)) {
                SysRoleSystemMenu systemMenu = new SysRoleSystemMenu();
                systemMenu.getParams().put("userId", SecurityUtils.getUserId());
                list = roleSystemMenuService.selectPermitPersonal(systemMenu);
            }
            if (StringUtils.equalsAny(system.getSearchValue(), MenuConstants.PERMIT_PERSONAL_SCREEN_DERIVE, MenuConstants.PERMIT_PERSONAL)) {
                checkSystem.getParams().put("onlyList", list);
                checkMenu.getParams().put("onlyList", list);
            } else {
                checkSystem.getParams().put("excludeList", list);
                checkMenu.getParams().put("excludeList", list);
            }
        }
        checkSystem.setStatus(system.getStatus());
        checkMenu.setStatus(system.getStatus());
        checkMenu.setMenuId(system.getSnowflakeId());
        // 查询系统信息列表
        List<SysSystem> systemList = StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_ALL_ONLY_PUBLIC) ? systemMapper.buildSystemMenuTreeSelectOnlyPublic(checkSystem) : systemMapper.buildSystemMenuTreeSelect(checkSystem);
        // 查询菜单信息列表
        List<SysMenu> menuList = StringUtils.equals(system.getSearchValue(), MenuConstants.PERMIT_ALL_ONLY_PUBLIC) ? menuMapper.buildSystemMenuTreeSelectOnlyPublic(checkMenu) : menuMapper.buildSystemMenuTreeSelect(checkMenu);
        List<SystemMenuVo> systemMenuList = new ArrayList<>();
        // 遍历系统列表并添加进系统-菜单树中
        for (SysSystem sys : systemList) {
            SystemMenuVo systemVo = new SystemMenuVo();
            systemVo.setUid(sys.getSystemId());
            systemVo.setFUid(MenuConstants.SYSTEM_TOP_NODE);
            systemVo.setName(sys.getName());
            systemVo.setStatus(sys.getStatus());
            systemVo.setType("0");
            systemVo.setSystemId(sys.getSystemId());
            systemVo.setIsCommon(sys.getIsCommon());
            systemMenuList.add(systemVo);
        }
        // 遍历菜单列表并添加进系统-菜单组装列表中
        for (SysMenu menu : menuList) {
            SystemMenuVo menuVo = new SystemMenuVo();
            menuVo.setUid(menu.getMenuId());
            menuVo.setFUid(menu.getParentId());
            menuVo.setName(menu.getMenuName());
            menuVo.setStatus(menu.getStatus());
            menuVo.setPerms(menu.getPerms());
            menuVo.setIcon(menu.getIcon());
            menuVo.setComponent(menu.getComponent());
            menuVo.setSystemId(menu.getSystemId());
            menuVo.setIsCommon(menu.getIsCommon());
            menuVo.setType("1");
            systemMenuList.add(menuVo);
        }
        List<SystemMenuVo> trees = buildSystemMenuTree(systemMenuList);
        // 干掉父级不是顶级节点的
        deleteNoTopNode(trees);
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 删除顶级节点非 SYSTEM_TOP_NODE 的值
     *
     * @param systemMenuList 系统-菜单组装列表
     */
    public void deleteNoTopNode(List<SystemMenuVo> systemMenuList) {
        systemMenuList.removeIf(systemMenuVo -> !Objects.equals(systemMenuVo.getFUid(), MenuConstants.SYSTEM_TOP_NODE));
    }

    /**
     * 构建前端所需要树结构
     *
     * @param systemMenuList 系统-菜单组装列表
     * @return 树结构列表
     */
    @Override
    public List<SystemMenuVo> buildSystemMenuTree(List<SystemMenuVo> systemMenuList) {
        List<SystemMenuVo> returnList = new ArrayList<SystemMenuVo>();
        List<Long> tempList = new ArrayList<Long>();
        for (SystemMenuVo systemMenuVo : systemMenuList) {
            tempList.add(systemMenuVo.getUid());
        }
        for (Iterator<SystemMenuVo> iterator = systemMenuList.iterator(); iterator.hasNext(); ) {
            SystemMenuVo systemMenuVo = (SystemMenuVo) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(systemMenuVo.getFUid())) {
                recursionFn(systemMenuList, systemMenuVo);
                returnList.add(systemMenuVo);
            }
        }
        if (returnList.isEmpty()) {
            returnList = systemMenuList;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SystemMenuVo> list, SystemMenuVo t) {
        // 得到子节点列表
        List<SystemMenuVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SystemMenuVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SystemMenuVo> getChildList(List<SystemMenuVo> list, SystemMenuVo t) {
        List<SystemMenuVo> tList = new ArrayList<SystemMenuVo>();
        Iterator<SystemMenuVo> it = list.iterator();
        while (it.hasNext()) {
            SystemMenuVo n = (SystemMenuVo) it.next();
            if (StringUtils.isNotNull(n.getFUid()) && n.getFUid().longValue() == t.getUid().longValue()) {
                tList.add(n);
            }
        }
        return tList;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SystemMenuVo> list, SystemMenuVo t) {
        return getChildList(list, t).size() > 0;
    }
}
