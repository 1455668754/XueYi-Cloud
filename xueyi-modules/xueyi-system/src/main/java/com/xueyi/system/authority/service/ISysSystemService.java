package com.xueyi.system.authority.service;

import com.xueyi.system.api.domain.authority.SysSystem;
import com.xueyi.system.authority.domain.SysMenu;
import com.xueyi.system.authority.domain.SystemMenuVo;
import com.xueyi.system.role.domain.SysRoleSystemMenu;
import com.xueyi.system.utils.vo.TreeSelect;

import java.util.List;

/**
 * 子系统模块Service接口
 *
 * @author xueyi
 */
public interface ISysSystemService {

    /**
     * 当前用户首页可展示的模块列表
     *
     * @return 子系统模块集合
     */
    public List<SysSystem> homePageView();

    /**
     * 当前用户Id查询模块&&菜单 | 非管理员
     *
     * @param menu 菜单信息 | null
     * @return 模块&&菜单列表
     */
    public List<SysRoleSystemMenu> userHomePageView(SysMenu menu);

    /**
     * 查询子系统模块
     *
     * @param system 子系统模块 | systemId 子系统模块Id
     * @return 子系统模块
     */
    public SysSystem selectSystemById(SysSystem system);

    /**
     * 查询子系统模块列表
     *
     * @param system 子系统模块
     * @return 子系统模块集合
     */
    public List<SysSystem> selectSystemList(SysSystem system);

    /**
     * 新增子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int insertSystem(SysSystem system);

    /**
     * 修改子系统模块
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int updateSystem(SysSystem system);

    /**
     * 修改子系统模块状态
     *
     * @param system 子系统模块
     * @return 结果
     */
    public int updateSystemStatus(SysSystem system);

    /**
     * 批量删除子系统模块
     *
     * @param system 子系统模块 | params.Ids 需要删除的子系统模块Ids组
     * @return 结果
     */
    public int deleteSystemByIds(SysSystem system);

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
    public List<TreeSelect> buildSystemMenuTreeSelect(SysSystem system);

    /**
     * 构建前端所需要树结构
     *
     * @param systemMenuList 系统-菜单数组装列表
     * @return 树结构列表
     */
    public List<SystemMenuVo> buildSystemMenuTree(List<SystemMenuVo> systemMenuList);
}
