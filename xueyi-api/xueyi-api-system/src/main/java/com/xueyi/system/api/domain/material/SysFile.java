package com.xueyi.system.api.domain.material;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件信息
 *
 * @author xueyi
 */
public class SysFile {

    /** 文件Id */
    private Long id;

    /** 文件夹Id */
    private Long fid;

    /** 文件别称 */
    private String nick;

    /** 文件名称 */
    private String name;

    /** 文件地址 */
    private String url;

    /** 文件地址 */
    private float size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getName())
                .append("fid", getFid())
                .append("nick", getNick())
                .append("name", getName())
                .append("url", getUrl())
                .append("size", getSize())
                .toString();
    }
}
