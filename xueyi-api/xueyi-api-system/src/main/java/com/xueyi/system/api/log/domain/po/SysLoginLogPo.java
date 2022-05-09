package com.xueyi.system.api.log.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 访问日志 持久化对象
 *
 * @author xueyi
 */
public class SysLoginLogPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 企业账号 */
    @Excel(name = "企业账号")
    @TableField(value = "enterprise_name")
    private String enterpriseName;

    /** 用户Id */
    @TableField("user_id")
    private Long userId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    @TableField(value = "user_name", condition = LIKE)
    private String userName;

    /** 用户名称 */
    @Excel(name = "用户名称")
    @TableField(value = "user_nick", condition = LIKE)
    private String userNick;

    /** 状态 0成功 1失败 */
    @Excel(name = "状态", readConverterExp = "0=成功,1=失败")
    @TableField("status")
    private String status;

    /** 地址 */
    @Excel(name = "地址")
    @TableField(value = "ipaddr", condition = LIKE)
    private String ipaddr;

    /** 描述 */
    @Excel(name = "描述")
    @TableField("msg")
    private String msg;

    /** 访问时间 */
    @OrderBy(sort = 10)
    @TableField("access_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }
}
