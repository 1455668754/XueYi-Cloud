package com.xueyi.system.api.log.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 访问日志 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_login_log",excludeProperty = {"name","sort","createBy","createTime","updateBy","updateTime","remark"})
public class SysLoginLogDto extends SysLoginLogPo {

    private static final long serialVersionUID = 1L;

}
