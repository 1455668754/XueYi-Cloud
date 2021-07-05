package com.xueyi.system.monitor.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.system.api.monitor.SysOperLog;
import com.xueyi.system.monitor.mapper.SysOperLogMapper;
import com.xueyi.system.monitor.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysOperLogServiceImpl implements ISysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     * @return 结果
     */
    @Override
    @DS("#operLog.sourceName")
    public int insertOperlog(SysOperLog operLog) {
        return operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 查询操作日志详细
     *
     * @param operLog 操作日志对象 | operId 操作Id
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(SysOperLog operLog) {
        return operLogMapper.selectOperLogById(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operLog 操作日志对象 | params.Ids 需要删除的登录日志Ids组
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(SysOperLog operLog) {
        return operLogMapper.deleteOperLogByIds(operLog);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog(new SysOperLog());
    }
}
