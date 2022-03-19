package com.xueyi.job.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.manager.SubBaseManager;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.mapper.SysJobLogMapper;
import com.xueyi.job.mapper.SysJobMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 调度任务管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysJobManager extends SubBaseManager<SysJobDto, SysJobMapper, SysJobLogDto, SysJobLogMapper> {

    /**
     * 项目启动时
     */
    @TenantIgnore(tenantLine = true)
    public List<SysJobDto> initScheduler() {
        return baseMapper.selectList(Wrappers.query());
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysJobLogDto> queryWrapper, LambdaUpdateWrapper<SysJobLogDto> updateWrapper, SysJobDto job, Serializable key) {
        Serializable jobGroup = ObjectUtil.isNotNull(job) ? job.getJobGroup() : key;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysJobLogDto::getJobGroup, jobGroup);
        else
            updateWrapper.eq(SysJobLogDto::getJobGroup, jobGroup);
    }
}
