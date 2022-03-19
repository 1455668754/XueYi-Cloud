package com.xueyi.system.notice.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.BaseEntity;

/**
 * 通知公告 持久化对象
 *
 * @author xueyi
 */
public class SysNoticePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 公告类型（0通知 1公告） */
    @Excel(name = "公告类型", readConverterExp = "0=通知,1=公告")
    @TableField("type")
    private String type;

    /** 公告内容 */
    @Excel(name = "公告内容")
    @TableField("content")
    private String content;

    /** 公告状态（0未发送 1已发送） */
    @Excel(name = "公告状态", readConverterExp = "0=未发送,1=已发送")
    @TableField("status")
    private String status;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
