package com.xueyi.system.cache.mapper;

import com.xueyi.system.api.domain.authority.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色缓存数据 数据层
 *
 * @author xueyi
 */
public interface SysRoleCacheMapper {

    /**
     * 查询所有角色信息 | 指定源所有企业
     *
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheListBySource();

    /**
     * 查询指定企业的所有角色信息 | 指定源指定企业
     *
     * @param enterpriseId 租户Id
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheListByEnterpriseId(Long enterpriseId);

    /**
     * 根据角色信息查找角色信息 | 单个企业的单个指定角色
     *
     * @param role 角色信息 | roleId 角色Id | enterpriseId 租户Id
     */
    public SysRole selectRoleCacheByRoleId(SysRole role);

    /**
     * 根据排除列表查询列表外角色信息 | 指定源
     *
     * @param enterpriseIds 排除集合 企业Ids
     * @param roleIds       排除集合 角色Ids
     * @return 通用缓存封装对象集合
     */
    public List<SysRole> selectRoleCacheListByExcludeIds(@Param("enterpriseIds") Set<Long> enterpriseIds, @Param("roleIds") Set<Long> roleIds);
}