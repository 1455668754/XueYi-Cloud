package com.xueyi.system.api.log.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.system.api.log.domain.po.SysOperateLogPo;

/**
 * 操作日志 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_operate_log",excludeProperty = {"name","sort","createBy","createTime","updateBy","updateTime","remark"})
public class SysOperateLogDto extends SysOperateLogPo {

    private static final long serialVersionUID = 1L;

    /** 业务类型数组 */
    @TableField(exist = false)
    private Integer[] businessTypes;

    public Integer[] getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes) {
        this.businessTypes = businessTypes;
    }

}
