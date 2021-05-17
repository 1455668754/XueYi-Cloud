package com.xueyi.system.dict.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.dict.domain.SysConfig;

/**
 * 参数配置 数据层
 *
 * @author ruoyi
 */
public interface SysConfigMapper {
    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数配置信息
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * 新增参数配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertConfig(SysConfig config);

    /**
     * 修改参数配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateConfig(SysConfig config);

    /**
     * 删除参数配置
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | configId 参数Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteConfigById(SysSearch search);

    /**
     * 批量删除参数信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | configIds 需要删除的参数Ids(Long[])
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteConfigByIds(SysSearch search);
}
