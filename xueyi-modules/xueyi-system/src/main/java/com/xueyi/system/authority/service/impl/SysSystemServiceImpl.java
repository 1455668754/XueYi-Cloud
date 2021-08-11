package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.MenuConstants;
import com.xueyi.common.core.utils.SecurityUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.authority.domain.SystemMenuVo;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysSystemMapper;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.role.domain.SysRoleSystemMenu;
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
    private SysMenuMapper menuMapper;

    @Autowired
    private SysSystemMapper systemMapper;

    @Autowired
    private ISysRoleSystemMenuService roleSystemMenuService;

    @Autowired
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private ISysSystemService systemService;

    /**
     * 当前用户首页可展示的模块列表
     *
     * @return 子系统模块集合
     */
    @Override
    public List<SysSystem> homePageView() {
        if (SysUser.isAdmin(SecurityUtils.getUserType())) {
            return systemMapper.AdminHomePageView(new SysSystem());
        }
        SysSystem sysSystem = new SysSystem();
        SysMenu menu = new SysMenu();
        menu.getParams().put("userId", SecurityUtils.getUserId());
        sysSystem.getParams().put("roleSystemPerms", systemService.userHomePageView(menu));
        return systemMapper.selectSystemViewList(sysSystem);
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
     * @param sysSystem 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    @Override
    public SysSystem selectSystemById(SysSystem sysSystem) {
        return systemMapper.selectSystemById(sysSystem);
    }

    /**
     * 查询子系统模块列表
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块
     */
    @Override
    public List<SysSystem> selectSystemList(SysSystem sysSystem) {
        return systemMapper.selectSystemList(sysSystem);
    }

    /**
     * 新增子系统模块
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    @Override
    public int insertSystem(SysSystem sysSystem) {
        return systemMapper.insertSystem(sysSystem);
    }

    /**
     * 修改子系统模块
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    @Override
    public int updateSystem(SysSystem sysSystem) {
        return systemMapper.updateSystem(sysSystem);
    }

    /**
     * 修改子系统模块状态
     *
     * @param sysSystem 子系统模块
     * @return 结果
     */
    public int updateSystemStatus(SysSystem sysSystem) {
        return systemMapper.updateSystemStatus(sysSystem);
    }

    /**
     * 批量删除子系统模块
     *
     * @param sysSystem 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    @Override
    public int deleteSystemByIds(SysSystem sysSystem) {
        return systemMapper.deleteSystemByIds(sysSystem);
    }

    /**
     * 加载角色系统-菜单列表树
     *
     * @param sysSystem 子系统模块 | Id exclude的模块&菜单Id | status 模块&菜单状态 | searchValue 查询类型
     *                  searchValue = PERMIT_ALL                        获取所有权限内模块&菜单 | 无衍生角色
     *                  searchValue = PERMIT_ADMINISTRATOR              仅获取超管权限内模块&菜单 | 衍生角色仅获取超管衍生
     *                  searchValue = PERMIT_ENTERPRISE                 仅获取租户权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *                  searchValue = PERMIT_PERSONAL_SCREEN_DERIVE     仅获取个人权限内模块&菜单 | 衍生角色仅获取超管衍生&租户衍生
     *                  searchValue = PERMIT_PERSONAL                   仅获取个人权限内模块&菜单 | 衍生角色获取自身组织衍生&超管衍生&租户衍生
     */
    @Override
    @Transactional
    @GlobalTransactional
    public List<TreeSelect> buildSystemMenuTreeSelect(SysSystem sysSystem) {
        List<SysRoleSystemMenu> list = null;
        SysSystem checkSystem = new SysSystem();
        SysMenu checkMenu = new SysMenu();
        if (StringUtils.isNotEmpty(sysSystem.getSearchValue()) && !StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_ALL)) {
            if (SysUser.isAdmin(SecurityUtils.getUserType()) || StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_ADMINISTRATOR)) {
                list = roleSystemMenuService.selectPermitAdministrator(new SysRoleSystemMenu());
            } else if (StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_ENTERPRISE)) {
                list = roleSystemMenuService.selectPermitEnterprise(new SysRoleSystemMenu());
            } else if (StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_PERSONAL_SCREEN_DERIVE)) {
                SysRoleSystemMenu systemMenu = new SysRoleSystemMenu();
                systemMenu.getParams().put("userId", SecurityUtils.getUserId());
                list = roleSystemMenuService.selectPermitPersonalScreenDerive(systemMenu);
            } else if (StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_PERSONAL)) {
                SysRoleSystemMenu systemMenu = new SysRoleSystemMenu();
                systemMenu.getParams().put("userId", SecurityUtils.getUserId());
                list = roleSystemMenuService.selectPermitPersonal(systemMenu);
            }
            if (StringUtils.equals(sysSystem.getSearchValue(), MenuConstants.PERMIT_PERSONAL_SCREEN_DERIVE)) {
                checkSystem.getParams().put("onlyList", list);
                checkMenu.getParams().put("onlyList", list);
            } else {
                checkSystem.getParams().put("excludeList", list);
                checkMenu.getParams().put("excludeList", list);
            }
        }
        checkSystem.setStatus(sysSystem.getStatus());
        checkMenu.setStatus(sysSystem.getStatus());
        checkMenu.setMenuId(sysSystem.getId());
        //查询系统信息列表
        List<SysSystem> systemList = systemMapper.selectSystemList(checkSystem);
        //查询菜单信息列表
        List<SysMenu> menuList = menuMapper.buildSystemMenuTreeSelect(checkMenu);
        List<SystemMenuVo> systemMenuList = new ArrayList<>();
        SystemMenuVo systemMenuVo;
        //创建初始系统信息并添加进list中
        systemMenuVo = new SystemMenuVo();
        systemMenuVo.setUid(0L);
        systemMenuVo.setFUid(MenuConstants.TOP_NODE);
        systemMenuVo.setName("默认系统");
        systemMenuVo.setStatus("0");
        systemMenuVo.setType("0");
        systemMenuVo.setSystemId(0L);
        systemMenuVo.setIsMain("1");
        systemMenuList.add(systemMenuVo);
        //遍历系统列表并添加进系统-菜单树中
        for (SysSystem system : systemList) {
            systemMenuVo = new SystemMenuVo();
            systemMenuVo.setUid(system.getSystemId());
            systemMenuVo.setFUid(MenuConstants.TOP_NODE);
            systemMenuVo.setName(system.getSystemName());
            systemMenuVo.setStatus(system.getStatus());
            systemMenuVo.setType("0");
            systemMenuVo.setSystemId(system.getSystemId());
            systemMenuVo.setIsMain("1");
            systemMenuList.add(systemMenuVo);
        }
        //遍历菜单列表并添加进系统-菜单组装列表中
        for (SysMenu menu : menuList) {
            systemMenuVo = new SystemMenuVo();
            systemMenuVo.setUid(menu.getMenuId());
            systemMenuVo.setFUid(menu.getParentId());
            systemMenuVo.setName(menu.getMenuName());
            systemMenuVo.setStatus(menu.getStatus());
            systemMenuVo.setPerms(menu.getPerms());
            systemMenuVo.setIcon(menu.getIcon());
            systemMenuVo.setComponent(menu.getComponent());
            systemMenuVo.setSystemId(menu.getSystemId());
            systemMenuVo.setIsMain(menu.getIsMain());
            systemMenuVo.setType("1");
            systemMenuList.add(systemMenuVo);
        }
        List<SystemMenuVo> trees = buildSystemMenuTree(systemMenuList);
        //干掉父级不是顶级节点的
        deleteNoTopNode(trees);
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 删除顶级节点非 TOP_NODE 的值
     *
     * @param systemMenuList 系统-菜单组装列表
     */
    public void deleteNoTopNode(List<SystemMenuVo> systemMenuList) {
        systemMenuList.removeIf(systemMenuVo -> !Objects.equals(systemMenuVo.getFUid(), MenuConstants.TOP_NODE));
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
