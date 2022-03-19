package com.xueyi.common.log.enums;

/**
 * 操作人类别
 *
 * @author xueyi
 */
public enum OperatorType {

    /** 其它 */
    OTHER("00", "其它"),

    /** 后台用户 */
    MANAGE("01", "后台用户"),

    /** 手机端用户 */
    MOBILE("02", "手机端用户");

    private final String code;
    private final String info;

    OperatorType(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
