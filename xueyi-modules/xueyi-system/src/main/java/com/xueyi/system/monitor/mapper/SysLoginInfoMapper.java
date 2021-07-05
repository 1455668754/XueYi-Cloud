package com.xueyi.system.monitor.mapper;

import java.util.List;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.monitor.domain.SysLoginInfo;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author xueyi
 */
public interface SysLoginInfoMapper {
    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    public int insertLoginInfo(SysLoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     * 访问控制 d 部门 | p 岗位 | u 用户 | li 租户查询
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    @DataScope(deptAlias = "d", postAlias = "p", userAlias = "u", eAlias = "li")
    public List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo);

    /**
     * 批量删除系统登录日志
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param loginInfo 访问日志对象 | params.Ids 需要删除的登录日志Ids组
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteLoginInfoByIds(SysLoginInfo loginInfo);

    /**
     * 清空系统登录日志
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param loginInfo 访问日志对象 | null
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int cleanLoginInfo(SysLoginInfo loginInfo);
}
