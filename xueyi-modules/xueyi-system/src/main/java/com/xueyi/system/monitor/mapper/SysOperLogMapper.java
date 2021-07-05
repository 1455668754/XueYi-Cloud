package com.xueyi.system.monitor.mapper;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.monitor.SysOperLog;

/**
 * 操作日志 数据层
 *
 * @author xueyi
 */
public interface SysOperLogMapper {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public int insertOperlog(SysOperLog operLog);

    /**
     * 查询系统操作日志集合
     * 访问控制 d 部门 | p 岗位 | u 用户 | ol 租户查询
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @DataScope(deptAlias = "d", postAlias = "p", userAlias = "u", eAlias = "ol")
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 查询操作日志详细
     * 访问控制 ol 租户查询
     *
     * @param operLog 操作日志对象 | operId 操作Id
     * @return 操作日志对象
     */
    @DataScope(eAlias = "ol")
    public SysOperLog selectOperLogById(SysOperLog operLog);

    /**
     * 批量删除系统操作日志
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param operLog 操作日志对象 | params.Ids 需要删除的登录日志Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteOperLogByIds(SysOperLog operLog);

    /**
     * 清空操作日志
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param operLog 操作日志对象 | null
     */
    @DataScope(ueAlias = "empty")
    public void cleanOperLog(SysOperLog operLog);
}
