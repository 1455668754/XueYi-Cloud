package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.authority.SysSystem;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.authority.domain.SystemMenuVo;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysSystemMapper;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.role.domain.SysRoleSystemMenu;
import com.xueyi.system.role.mapper.SysRoleSystemMenuMapper;
import com.xueyi.system.utils.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private SysRoleSystemMenuMapper roleSystemMenuMapper;

    @Autowired
    private ISysSystemService systemService;

    /**
     * 查询首页可展示子系统模块列表
     *
     * @param userId   当前用户Id
     * @param userType 用户标识
     * @return 子系统模块集合
     */
    @Override
    public List<SysSystem> selectSystemViewList(Long userId, String userType) {
        if (SysUser.isAdmin(userType)) {
            return systemMapper.selectSystemViewAdminList(new SysSystem());
        }
        SysSystem sysSystem = new SysSystem();
        sysSystem.getParams().put("roleSystemPerms", systemService.selectSystemMenuListByUserId(userId));
        return systemMapper.selectSystemViewList(sysSystem);
    }

    /**
     * 根据用户Id查询模块&&菜单
     *
     * @param userId 用户Id
     * @return 模块&&菜单列表
     */
    @Override
    @DS("#isolate")
    public List<SysRoleSystemMenu> selectSystemMenuListByUserId(Long userId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("userId", userId);
        return roleSystemMenuMapper.selectSystemMenuListByUserId(search);//@param search 万用组件 | userId 用户Id
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
     * 加载对应角色系统-菜单列表树 | searchValue === 0 仅查询所有正常模块&&菜单 | searchValue === 1 查询所有模块&&菜单 | Id exclude的菜单Id
     */
    @Override
    public List<TreeSelect> buildSystemMenuTreeSelect(SysSystem sysSystem) {
        SysSystem checkSystem = new SysSystem();
        SysMenu checkMenu = new SysMenu();
        if (sysSystem.getSearchValue().equals("0")) {
            checkSystem.setStatus("0");
            checkMenu.setStatus("0");
        } else if (sysSystem.getSearchValue().equals("1") && sysSystem.getId() != null && sysSystem.getId() != 0L) {
            checkMenu.setMenuId(sysSystem.getId());
        }
        //查询系统信息列表
        List<SysSystem> systemList = systemMapper.selectSystemList(checkSystem);
        //查询菜单信息列表
        List<SysMenu> menuList = menuMapper.selectMenuListAll(checkMenu);
        //将菜单列表中的顶级菜单的父级变更为对应系统ID，为下一步的系统-菜单列表组件提供有效参数
        for (SysMenu sysMenu : menuList) {
            if (sysMenu.getParentId().equals(0L)) {
                sysMenu.setParentId(sysMenu.getSystemId());
            }
        }
        List<SystemMenuVo> systemMenuList = new ArrayList<>();
        SystemMenuVo systemMenuVo;
        //创建初始系统信息并添加进list中
        systemMenuVo = new SystemMenuVo();
        systemMenuVo.setUid(0L);
        systemMenuVo.setFUid(-1L);
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
            systemMenuVo.setFUid(-1L);
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
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
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
