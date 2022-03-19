package com.xueyi.system.utils.route;

/**
 * 菜单标签配置信息
 *
 * @author xueyi
 */
public class TagVo {

    /** 为true则显示小圆点 */
    private Boolean dot;

    /** 内容 */
    private String content;

    /** 类型 'error' | 'primary' | 'warn' | 'success' */
    private String type;

    public Boolean getDot() {
        return dot;
    }

    public void setDot(Boolean dot) {
        this.dot = dot;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}