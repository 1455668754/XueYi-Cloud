package com.xueyi.common.message.domain;

import cn.hutool.core.util.IdUtil;

/**
 * 消息对象
 *
 * @author Ethereal
 */
public class Message {

    /** 消息Id */
    protected String id = IdUtil.randomUUID();

    /** 单个消息过期时间 */
    protected String expiration;

    /** 消息内容 */
    protected Object data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Message() {
    }

    public Message(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public Message(String id, Object data, String expiration) {
        this.id = id;
        this.data = data;
        this.expiration = expiration;
    }
}