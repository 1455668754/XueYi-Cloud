package com.xueyi.system.authority.service.impl;

import com.xueyi.system.api.authority.SysSystem;
import com.xueyi.system.authority.mapper.SysSystemMapper;
import com.xueyi.system.authority.service.ISysSystemService;
import com.xueyi.system.api.utilTool.SysSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 子系统模块Service业务层处理
 *
 * @author xueyi
 */
@Service
public class SysSystemServiceImpl implements ISysSystemService {
    @Autowired
    private SysSystemMapper systemMapper;

    /**
     * 查询首页可展示子系统模块列表
     *
     * @param sysSystem 子系统模块
     * @return 子系统模块
     */
    @Override
    public List<SysSystem> selectSystemViewList(SysSystem sysSystem) {
        return systemMapper.selectSystemViewList(sysSystem);
    }

    /**
     * 查询子系统模块
     *
     * @param systemId 子系统模块Id
     * @return 子系统模块
     */
    @Override
    public SysSystem selectSystemById(Long systemId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("systemId", systemId);
        return systemMapper.selectSystemById(search);
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
    public int updateSystemStatus(SysSystem sysSystem){
        return systemMapper.updateSystemStatus(sysSystem);
    }

    /**
     * 批量删除子系统模块
     *
     * @param systemIds 需要删除的子系统模块Id
     * @return 结果
     */
    @Override
    public int deleteSystemByIds(Long[] systemIds) {
        SysSearch search = new SysSearch();
        search.getSearch().put("systemIds", systemIds);
        return systemMapper.deleteSystemByIds(search);
    }
}
