package com.xueyi.system.api.file.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;

import java.math.BigDecimal;

/**
 * 素材 持久化对象
 *
 * @author xueyi
 */
public class SysMaterialPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 分类Id */
    @Excel(name = "分类Id")
    @TableField("folder_id")
    private Long folderId;

    /** 素材昵称 */
    @Excel(name = "素材昵称")
    @TableField("nick")
    private String nick;

    /** 素材名称 */
    @Excel(name = "原图名称")
    @TableField("original_name")
    private String originalName;

    /** 素材地址 */
    @Excel(name = "素材地址")
    @TableField("url")
    private String url;

    /** 原图地址 */
    @Excel(name = "原图地址")
    @TableField("original_url")
    private String originalUrl;

    /** 素材大小 */
    @Excel(name = "素材大小")
    @TableField("size")
    private BigDecimal size;

    /** 素材类型 0默认素材 1系统素材 */
    @Excel(name = "素材类型", readConverterExp = "0=默认素材,1=系统素材")
    @TableField("type")
    private String type;

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
