package com.xueyi.job.service;

import com.xueyi.common.core.exception.job.TaskException;
import com.xueyi.job.api.domain.dto.SysJobDto;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 调度任务管理 服务层
 *
 * @author xueyi
 */
public interface ISysJobService {

    /**
     * 查询调度任务对象列表 | 数据权限 | 附加数据
     *
     * @param job 调度任务对象
     * @return 调度任务对象集合
     */
    List<SysJobDto> selectListScope(SysJobDto job);

    /**
     * 根据Id查询单条调度任务对象
     *
     * @param id Id
     * @return 调度任务对象
     */
    SysJobDto selectById(Long id);

    /**
     * 新增调度任务对象
     *
     * @param job 调度任务对象
     * @return 结果
     */
    int insert(SysJobDto job) throws SchedulerException, TaskException;

    /**
     * 修改调度任务对象
     *
     * @param job 调度任务对象
     * @return 结果
     */
    int update(SysJobDto job) throws SchedulerException, TaskException;

    /**
     * 修改调度任务对象状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    int updateStatus(Long id, String status) throws SchedulerException;

    /**
     * 根据Id集合删除调度任务对象
     *
     * @param idList Id集合
     * @return 结果
     */
    int deleteByIds(List<Long> idList) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int pauseJob(SysJobDto job) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int resumeJob(SysJobDto job) throws SchedulerException;

    /**
     * 立即运行任务
     *
     * @param id Id
     */
    void run(Long id) throws SchedulerException;

}